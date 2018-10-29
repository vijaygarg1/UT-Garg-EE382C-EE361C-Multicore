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

__global__ void sum_c(int *B, int *C) {
    if (threadIdx.x < 10) C[threadIdx.x] = B[threadIdx.x];
    __syncthreads();
    for (int d = 1; d < 10; d*=2) {
        int val;
        if (threadIdx.x >= d) {
            val = C[threadIdx.x - d];
        }
        __syncthreads();
        if (threadIdx.x >= d) {
            C[threadIdx.x] += val;
        }
        __syncthreads();
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

int* b(vector<int> arr, int len) {
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

    int* retval = new int[10];
    copy(B, B+10, retval);
    return retval;
}

void c(int * B) {
    for (int i = 0; i < 10; i++) {
        cout << i << " old: " << B[i] << endl;
    }
    int *d_B; int *d_C;
    int size = 10 * sizeof(int);
    int *C = (int*)malloc(size);
    cudaMalloc((void**)&d_B, size);
    cudaMalloc((void**)&d_C, size);
    cudaMemcpy(d_B, B, size, cudaMemcpyHostToDevice);

    sum_c<<<1,10>>>(d_B, d_C);

    cudaMemcpy(C, d_C, size, cudaMemcpyDeviceToHost);

    for (int i = 0; i < 10; i++) {
        cout << i << ": " << C[i] << endl;
    }

    cudaFree(d_B); cudaFree(d_C);
    free(C);
}

int main () {
    vector<int> arr;
    int len = 0;
    if (!populate_array(&arr, &len)) {
      return 0;
    }

    a(arr, len);
    int* B = b(arr, len);
    c(B);
    delete [] B;
}