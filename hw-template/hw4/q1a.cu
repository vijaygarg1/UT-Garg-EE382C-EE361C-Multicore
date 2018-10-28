#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include "vector"
#include <math.h>
#include <cmath>
using namespace std;

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

__global__ void min_seq(int *arr, int *seq_result, int chunk_len, int full_len) {
  // TODO: Iterate over chunk until done or until N is reached
}

int main () {
  vector<int> arr;
  int len = 0;
  if (!populate_array(&arr, &len)) {
    return 0;
  }
  cout<<arr[0]<<endl;
  cout<<len<<endl;
  cout<<log(log(len))<<endl;

  int N = (int) (len / log(log(len)))

  int full_size = len * sizeof(int);
  int seq_result_size = N * sizeof(int);

  // Full array and result of sequential phase
  int *d_arr;
  int *seq_result;
  int *d_seq_result;

  cudaMalloc((void **)&d_arr, full_size);
  cudaMalloc((void **)&d_a_result, seq_result_size)

  seq_result = (int *)malloc(seq_result_size);

  // Copy full array
  cudaMemcpy(d_arr, arr.data(), full_size, cudaMemcpyHostToDevice);

  // TODO: Figure out if rounding will cause any missed elements
  min_seq<<<N, 1>>>(d_arr, d_seq_result, );

  // Save results of sequential phase
  cudaMemcpy(seq_result, d_seq_result, seq_result_size, cudaMemcpyDeviceToHost);

  // Cleanup
  free(seq_result);
  cudaFree(d_arr); cudaFree(d_seq_result);



}
