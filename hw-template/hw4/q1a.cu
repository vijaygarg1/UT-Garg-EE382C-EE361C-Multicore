#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include "vector"
using namespace std;

int populate_array(vector<string>* arr) {
  ifstream infile( "inp.txt" );
  if (!infile.is_open()) {
    cout<<"File failed to open"<<endl;
    return 0;
  }
  string line;
  while (getline(infile, line))
  {
    istringstream ss(line);

    vector <string> record;

    while (ss)
    {
      string s;
      if (!getline(ss, s, ',')) break;
      arr->push_back( s );
    }
  }
  return 1;
}

int main () {
  vector<string> arr;
  if (!populate_array(&arr)) {
    return 0;
  }
  cout<<arr[0]<<endl;
}
