#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <string.h>

void MatrixMult(char file1[],char file2[],int T)
{
//Write your code here
}

void main(int argc, char *argv[])
{
  char *file1, *file2;
  file1=argv[1];
  file2=argv[2];
  int T=atoi(argv[3]);
  MatrixMult(file1,file2,T);
}


