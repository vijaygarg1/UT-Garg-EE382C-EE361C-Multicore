#include <omp.h>
#include <stdio.h>
#define CHUNKSIZE 10
#define N     100

main ()  
{

int i, chunk;
float a[N], b[N], c[N];

/* Some initializations */
for (i=0; i < N; i++)
  a[i] = b[i] = i * 1.0;
chunk = CHUNKSIZE;

#pragma omp parallel shared(a,b,c,chunk) private(i)
  {

  #pragma omp for schedule(dynamic,chunk)
  for (i=0; i < N; i++)
    c[i] = a[i] + b[i];

  }  /* end of parallel section */

  for (i=0; i<N; i++)
     printf("c[%d] = %3.1f\n", i, c[i]);

}
