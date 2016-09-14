// OpenMP example program:  Dijkstra shortest-path finder in a
// bidirectional graph

// serves as a tutorial to OpenMP; see notes in comments at the end of
// the file

// each thread handles one chunk of vertices

// usage:  dijkstra 

#include <stdio.h>

#define LARGEINT 2<<30-1  // "infinity"
#define NV 6

// global variables, all shared by all threads by default

int ohd[NV][NV],  // 1-hop distances between vertices
    mind[NV],  // min distances found so far
    notdone[NV], // vertices not checked yet
    nth,  // number of threads
    chunk,  // number of vertices handled by each thread
    md,  // current min over all threads
    mv;  // vertex which achieves that min

void init(int ac, char **av)
{  int i,j;
   for (i = 0; i < NV; i++)  
      for (j = 0; j < NV; j++)  {
         if (j == i) ohd[i][i] = 0;
         else ohd[i][j] = LARGEINT;
      }
   ohd[0][1] = ohd[1][0] = 40;
   ohd[0][2] = ohd[2][0] = 15;
   ohd[1][2] = ohd[2][1] = 20;
   ohd[1][3] = ohd[3][1] = 10;
   ohd[1][4] = ohd[4][1] = 25;
   ohd[2][3] = ohd[3][2] = 100;
   ohd[1][5] = ohd[5][1] = 6;
   ohd[4][5] = ohd[5][4] = 8;
   for (i = 1; i < NV; i++)  {
      notdone[i] = 1;
      mind[i] = ohd[0][i];
   }
}

// finds closest to 0 among notdone, among s through e
void findmymin(int s, int e, int *d, int *v)
{  int i;
      *d = LARGEINT; 
      for (i = s; i <= e; i++)
         if (notdone[i] && mind[i] < *d)  {
            *d = ohd[0][i];
            *v = i;
         }
}

// for each i in [s,e], ask whether a shorter path to i exists, through
// mv
void updateohd(int s, int e)
{  int i;
   for (i = s; i <= e; i++)
      if (mind[mv] + ohd[mv][i] < mind[i])  
         mind[i] = mind[mv] + ohd[mv][i];
}

void dowork()
{  
   #pragma omp parallel  // Note 1  
   {  int startv,endv,  // start, end vertices for this thread
          step,  // whole procedure goes NV steps
          mymd,  // min value found by this thread
          mymv,  // vertex which attains that value
          me = omp_get_thread_num();  // my thread number
      #pragma omp single   // Note 2
      {  nth = omp_get_num_threads();  chunk = NV/nth;  
         printf("there are %d threads\n",nth);  }
      // Note 3
      startv = me * chunk; 
      endv = startv + chunk - 1;
      for (step = 0; step < NV; step++)  {
         // find closest vertex to 0 among notdone; each thread finds
         // closest in its group, then we find overall closest
         #pragma omp single 
         {  md = LARGEINT; mv = 0;  }
         findmymin(startv,endv,&mymd,&mymv);
         // update overall min if mine is smaller
         #pragma omp critical  // Note 4
         {  if (mymd < md)  
              {  md = mymd; mv = mymv;  }
         }
         // mark new vertex as done 
         #pragma omp single 
         {  notdone[mv] = 0;  }
         // now update my section of ohd
         updateohd(startv,endv);
         #pragma omp barrier 
      }
   }
}

int main(int argc, char **argv)
{  int i;
   init(argc,argv);
   dowork();  
   // back to single thread now
   printf("minimum distances:\n");
   for (i = 1; i < NV; i++)
      printf("%d\n",mind[i]);
}

// tutorial notes:  

//  1.  OpenMP works via a preprocessor, which translates pragmas to
//      threads calls.  Note that the sharp sign ('#') must be the first
//      character in the line, other than blanks.
//
//      The "parallel" clause says, "Have each thread do this block" 
//      (enclosed by braces).  Code not in a block with a "parallel" 
//      pragma is done only by the master thread.

//  2.  The "single" clause says, "Have only one thread (whichever hits
//      this line first) execute the following block."

//      In this case, we are calling the OMP function
//      omp_get_num_threads(), which of course returns the number of
//      threads.  Since we assign the return value to the global variable
//      nth, only one thread needs to do this, so we use "single".  And
//      thought there would be no harm (other than a delay) if all
//      threads did this, in some applications we would need to limit an
//      action to just one thread.

//  3.  The "barrier" clause does the standard barrier operation.  Note
//      carefully that there are also implicit barriers following blocks
//      to which various OpenMP pragmas apply, such as "for" and
//      "single".  One can override those implicit barriers by using the
//      "nowait" clause.  On platforms with nonsequential memory
//      consistency, you can also use the "flush" directive to force a
//      memory update.

//  4.  The "critical" clause sets up a critical section, with invisible
//      lock/unlock operations.  Note carefully that the clause may be
//      followed by an optional name, which is crucial in some
//      applications.  All critical sections with the same name 
//      are guarded by the same (invisible) locks.  Those with 
//      no name are also guarded by the same locks, so the programmer
//      could really lose parallelism if he/she were not aware of this.

//      Certain very specialized one-statement critical sections can be
//      handled more simply and efficiently using the "atomic"
//      directive, e.g.

//         #pragma omp atomic
//         y += x;

//      Note that that statment can NOT be a block.
