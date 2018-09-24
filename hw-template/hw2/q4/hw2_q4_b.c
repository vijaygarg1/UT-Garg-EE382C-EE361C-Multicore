#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>

double rand_1() {
    return (double)rand() / (double) RAND_MAX;
}

double MonteCarlo(int s)
{
    srand(time(0));
    int max = omp_get_max_threads();

    // Initialize array to 0s
    int inners[max];
    for (int i = 0; i < max; i++) inners[i] = 0;

    #pragma omp parallel for
    for (int i = 0; i < s; i++) {
        int id = omp_get_thread_num();
        double x = rand_1();
        double y = rand_1();
        double rad = x*x + y*y;
        if (rad <= 1) {
            #pragma omp atomic
            inners[id]++;
        }
    }
    int inner = 0;
    for (int i = 0; i < max; i++) inner += inners[i];
    double est = 4.0 * (double)inner / (double)s;
    return est;
}

void main()
{
double pi;
pi=MonteCarlo(100000000);
printf("Value of pi is: %lf\n",pi);
}



