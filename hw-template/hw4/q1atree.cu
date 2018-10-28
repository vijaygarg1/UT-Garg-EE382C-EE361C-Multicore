#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include "vector"
#include <math.h>
#include <cmath>
using namespace std;

__global__ void min2(int *arr, int i) {
    int a = arr[2 * i * blockIdx.x]; int b = arr[2 * i * blockIdx.x + i];
    if (a < b) {
        arr[2 * i * blockIdx.x] = a;
    } else {
        arr[2 * i * blockIdx.x] = b;
    }
    // if (2 * blockIdx.x + 1 >= len) {
    //     result[blockIdx.x] = arr[2 * blockIdx.x];
    // } else {
    //     a = arr[2 * blockIdx.x]; b = arr[2 * blockIdx.x + 1];
    //     if (a < b) {
    //         result[blockIdx.x] = a;
    //     } else {
    //         result[blockIdx.x] = b;
    //     }
    // }
}

int round_up(double val) {
    if (fabs(val-(int)val) < 0.00001) {
        return (int) val;
    }
    else return (int) val + 1;
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

int main () {
    vector<int> arr;
    int len = 0;
    if (!populate_array(&arr, &len)) {
      return 0;
    }

    // int levels = round_up(log2(len));

    int full_size = len * sizeof(int);

    // Full array and result of sequential phase
    int *d_arr;

    cudaMalloc((void **)&d_arr, full_size);
    cudaMemcpy(d_arr, arr.data(), full_size, cudaMemcpyHostToDevice);
    int N = len/2

    int i = 1;
    while (2*i < len) {
        min2<<<N,1>>>(d_arr, i);

        // cudaMemcpy(result, d_result, n * sizeof(int), cudaMemcpyDeviceToHost);

        // Update
        i *=2 ;
        N = (int)((len + 1) / (2 * i));
    }
    fin_arr = (int *)malloc(full_size);
    cudaMemcpy(fin_arr, d_arr, full_size, cudaMemcpyDeviceToHost);
    // Sequential comp
    int min;
    int a = arr[2 * i]; int b = arr[2 * i * blockIdx.x + i];
    if (a < b) {
        min = a;
    } else {
        min = b;
    }
    cout<<min<<endl;
    return 0;

    // for (int i = 0; i < levels; i++) {
    //     int *d_result;
    //     int *result;
    //     int n = round_up(len / pow(2, 1+i));

        
    //     cudaMalloc((void **)&d_result, n * sizeof(int));

    //     result = (int *)malloc(n * sizeof(int));

    //     min2<<<n, 1>>>(d_arr, d_result, len);

    //     // Save results
    //     cudaMemcpy(result, d_result, n * sizeof(int), cudaMemcpyDeviceToHost);

    //     // // Use results as new source
    //     arr.assign(result, result + n * sizeof(int))
    //     arr.resize(n * sizeof(int));
    //     full_size = n * sizeof(int);
        
    //     // Cleanup
    //     free(result);
    //     cudaFree(d_result);
    // }
    // cudaFree(d_arr); 
}