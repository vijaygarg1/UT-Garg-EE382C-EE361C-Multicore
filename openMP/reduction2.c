#include <omp.h>
#include <stdio.h>

#define SIZE 1000000

main ()  {

int   i, n, chunk;
float a[SIZE], b[SIZE], result;

/* Some initializations */
n = SIZE;
chunk = 10;
result = 0.0;
for (i=0; i < n; i++)
  {
  a[i] = i * 1.0;
  b[i] = i * 2.0;
  }

#pragma omp parallel for      \  
  default(shared) private(i)  \  
  schedule(static,chunk)      \  
  reduction(+:result)  

  for (i=0; i < n; i++)
    result = result + (a[i] * b[i]);

printf("Final result= %f\n",result);

}
