#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include "vector"
#include <math.h>
#include <cmath>
using namespace std;

__global__ void min2(int *arr, int *i) {
    int a = arr[2 * *i * blockIdx.x]; int b = arr[2 * *i * blockIdx.x + *i];
    if (a < b) {
        arr[2 * *i * blockIdx.x] = a;
    } else {
        arr[2 * *i * blockIdx.x] = b;
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
    int full_size = len * sizeof(int);

    // Full array
    int *d_arr;

    cudaMalloc((void **)&d_arr, full_size);
    
    int N = len/2;

    int i = 1;
    int *d_i;
    cudaMalloc((void **)&d_i, sizeof(int));
    while (2*i < len) {
        // Copy array and i over
        cudaMemcpy(d_arr, arr.data(), full_size, cudaMemcpyHostToDevice);
        cudaMemcpy(d_i, &i, sizeof(int), cudaMemcpyHostToDevice);

        min2<<<N,1>>>(d_arr, d_i);

        // Update
        i *= 2 ;
        N = (int)((len + 1) / (2 * i));
        cudaMemcpy(arr.data(), d_arr, full_size, cudaMemcpyDeviceToHost);
    }
    
    // Sequential compare
    int min;
    int a = arr[0]; int b = arr[i];
    if (a < b) {
        min = a;
    } else {
        min = b;
    }

    cout<<"Minimum: " << min << endl;

    cudaFree(d_arr); cudaFree(d_i);
}

int main () {
    vector<int> arr;
    int len = 0;
    if (!populate_array(&arr, &len)) {
      return 0;
    }

    a(arr, len);
    
    return 0;
}