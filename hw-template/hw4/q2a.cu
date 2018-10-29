#include<atomic>
#include <iostream>
#include "vector"
#include <fstream>
#include <sstream>
#include <string>
using namespace std;

__global__ void count_a(int *arr, int *B, int *chunk_len, int *len) {
    int start = threadIdx.x * *chunk_len;
    int i = start;
    while (i - start < *chunk_len && i < *len) {
        atomicAdd(B + (arr[i] / 100), 1);
        i++;
    }
}

__global__ void count_b(int *arr, int *B, int *chunk_len, int *len) {
    // Initialize shared array
    __shared__ int s_B[10];
    for (int i = threadIdx.x; i < 10; i += 1) {
        s_B[i] = 0;
    }
    __syncthreads();

    int index = threadIdx.x + blockIdx.x * blockDim.x;
    int start = index * *chunk_len;
    int i = start;
    while(i - start < *chunk_len && i < *len) {
        atomicAdd(s_B + (arr[i] / 100), 1);
        i++;
    }
    __syncthreads();
    if (threadIdx.x==0) {
        for (int j = 0; j < 10; j++) {
            atomicAdd(B + j, s_B[j]);
        }
    }
}

int populate_array(vector<int>* arr, int* len) {
    ifstream infile( "inp.txt" );
    if (!infile.is_open()) {
        cout<<"File failed to open"<<endl;
        return 0;
    }
    string line;
    while (getline(infile, line))
    {
        istringstream ss(line);
        while (ss)
        {
            string s;
            if (!getline(ss, s, ',')) break;

            (*len)++;
            arr->push_back(stoi(s));

        }
    }
    return 1;
}

void a(vector<int> arr, int len) {
    int size = 10 * sizeof(int);
    int full_size = len * sizeof(int);

    int B[10] = {};

    int *d_B;
    cudaMalloc((void**)&d_B, size);

    int *d_arr;
    cudaMalloc((void **)&d_arr, full_size);

    cudaMemcpy(d_arr, arr.data(), full_size, cudaMemcpyHostToDevice);


    cudaMemcpy(d_B, B, size, cudaMemcpyHostToDevice);
    int chunk_len = (int)(len/100) + 1;
    int *d_chunk_len;
    int *d_len;
    cudaMalloc((void **)&d_chunk_len, sizeof(int));
    cudaMalloc((void **)&d_len, sizeof(int));
    cudaMemcpy(d_chunk_len, &chunk_len, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(d_len, &len, sizeof(int), cudaMemcpyHostToDevice);

    cout<< chunk_len<<endl;

    count_a<<<1, 100>>>(d_arr, d_B, d_chunk_len, d_len);

    cudaMemcpy(B, d_B, size, cudaMemcpyDeviceToHost);

    for (int i = 0; i < 10; i++) {
        cout << i << ": " << B[i] << endl;
    }
    cudaFree(d_arr); cudaFree(d_B); cudaFree(d_chunk_len); cudaFree(d_len);
}

void b(vector<int> arr, int len) {
    int size = 10 * sizeof(int);
    int full_size = len * sizeof(int);

    int B[10] = {};

    int *d_B;
    cudaMalloc((void**)&d_B, size);

    int *d_arr;
    cudaMalloc((void **)&d_arr, full_size);

    cudaMemcpy(d_arr, arr.data(), full_size, cudaMemcpyHostToDevice);

    // Number of threads per block
    int thds = (int) (len / 20) + 1;

    cudaMemcpy(d_B, B, size, cudaMemcpyHostToDevice);
    int chunk_len = (int) len/(20 * thds) + 1;
    int *d_chunk_len;
    int *d_len;
    cudaMalloc((void **)&d_chunk_len, sizeof(int));
    cudaMalloc((void **)&d_len, sizeof(int));
    cudaMemcpy(d_chunk_len, &chunk_len, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(d_len, &len, sizeof(int), cudaMemcpyHostToDevice);
    
    count_b<<<20, thds>>>(d_arr, d_B, d_chunk_len, d_len);
    cudaMemcpy(B, d_B, size, cudaMemcpyDeviceToHost);

    for (int i = 0; i < 10; i++) {
        cout << i << ": " << B[i] << endl;
    }
    cudaFree(d_arr); cudaFree(d_B); cudaFree(d_chunk_len); cudaFree(d_len);
}

int main () {
    vector<int> arr;
    int len = 0;
    if (!populate_array(&arr, &len)) {
      return 0;
    }

    a(arr, len);
    b(arr, len);
}