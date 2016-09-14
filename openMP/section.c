#include <omp.h>
#include <stdio.h>
#define N     100
#define chunk  10

main ()
{

int i;
float a[N], b[N], c[N], d[N];

/* Some initializations */
for (i=0; i < N; i++) {
  // a[i] = i * 1.5;
  // b[i] = i + 22.35;
   a[i] = i * 1.0;
   b[i] = i * 1.0;
  }

#pragma omp parallel shared(a,b,c,d) private(i)
  {

  #pragma omp sections nowait
    {

    #pragma omp section
    for (i=0; i < N; i++)
      c[i] = a[i] + b[i];

    #pragma omp section
    for (i=0; i < N; i++)
      d[i] = a[i] * b[i];

    }  /* end of sections */

  }  /* end of parallel section */

   
#pragma omp parallel shared(a,b,c,d) private(i) 
  {
  #pragma omp for schedule(dynamic,chunk) nowait
  for (i=0; i<N; i++)
     printf("c[%d] = %3.1f ; d[%d] = %4.2f \n", i, c[i],i,d[i]);
  }

}
