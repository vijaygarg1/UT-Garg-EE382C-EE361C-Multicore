#include <omp.h>
#include <stdio.h>

main()
{

int x;
x = 0;

#pragma omp parallel shared(x) 
  {

  #pragma omp critical 
  x = x + 1;

  }  /* end of parallel section */

  printf("x = %d \n", x);

}
