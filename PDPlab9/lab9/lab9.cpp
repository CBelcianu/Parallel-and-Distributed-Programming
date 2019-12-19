#include <iostream>
#include <vector>
#include "mpi.h"

using namespace std;

inline void send_work(vector <int>& a, vector <int>& b, int nrProcs) {
	int n = a.size();
	for (int i = 1; i < nrProcs; ++i) {
		MPI_Send(&n, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
		MPI_Send(a.data(), n, MPI_INT, i, 1, MPI_COMM_WORLD);
		MPI_Send(b.data(), n, MPI_INT, i, 2, MPI_COMM_WORLD);
	}
	cout << "master sent work\n";
}

inline void collect(int nrProcs, vector <int>& res) {
	int l = res.size();
	for (int i = 1; i < nrProcs; ++i) {
		MPI_Status _;
		MPI_Recv(res.data(), l, MPI_INT, i, 3, MPI_COMM_WORLD, &_);
	}
	cout << "master collected the results\n";
}

inline void naive_multiply(vector<int> a, vector<int> b, vector<int> &prod, int size)
{
	for (int i = 0; i < size; i++)
	{
		for (int j = 0; j < size; j++)
			prod[i + j] += a[i] * b[j];
	}
}

inline vector<int> karatsuba_multiply(vector<int> a, vector<int> b, int posA, int posB, int size) {
	if (size == 1)
	{
		vector<int> res(a.size());
		res[0] = a[posA] * b[posB];
		return res;
	}

	vector<int> x = karatsuba_multiply(a, b, posA, posB, size / 2);
	vector<int> y = karatsuba_multiply(a, b, posA + size / 2, posB + size / 2, size / 2);
	vector<int> AhAl(a.size());
	vector<int> BhBl(a.size());

	for (int i = 0; i < size / 2; i++) {
		AhAl[i] = a[posA + i] + a[posA + i + (size / 2)];
		BhBl[i] = b[posB + i] + b[posB + i + (size / 2)];
	}

	vector<int> z = karatsuba_multiply(AhAl, BhBl, 0, 0, size / 2);
	vector<int> c(2 * a.size());

	for (int i = 0; i < size; i++) {
		c[i] += x[i];
		c[size / 2 + i] += z[i];
		c[size / 2 + i] -= x[i];
		c[size / 2 + i] -= y[i];
		c[i + size] = y[i];
	}

	return c;
}

inline void worker_naive(int me) {
	cout << "worker_naive(" << me << ") started\n";
	int n;
	MPI_Status _;
	MPI_Recv(&n, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &_);
	vector <int> a(n);
	vector <int> b(n);
	MPI_Recv(a.data(), n, MPI_INT, 0, 1, MPI_COMM_WORLD, &_);
	MPI_Recv(b.data(), n, MPI_INT, 0, 2, MPI_COMM_WORLD, &_);
	vector <int> res(2*n-1);
	naive_multiply(a, b, res, n);
	MPI_Send(res.data(), 2*n-1, MPI_INT, 0, 3, MPI_COMM_WORLD);
	cout << "worker(" << me << ") finished\n";
}

inline void worker_karatsuba(int me) {
	cout << "worker_karatsuba(" << me << ") started\n";
	int n;
	MPI_Status _;
	MPI_Recv(&n, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &_);
	vector <int> a(n);
	vector <int> b(n);
	MPI_Recv(a.data(), n, MPI_INT, 0, 1, MPI_COMM_WORLD, &_);
	MPI_Recv(b.data(), n, MPI_INT, 0, 2, MPI_COMM_WORLD, &_);
	vector<int> res = karatsuba_multiply(a, b, 0, 0, n);
	MPI_Send(res.data(), 2 * n - 1, MPI_INT, 0, 3, MPI_COMM_WORLD);
	cout << "worker(" << me << ") finished\n";
}

int main()
{
	MPI_Init(0, 0);
	int me;
	int nrProcs;
	MPI_Comm_size(MPI_COMM_WORLD, &nrProcs);
	MPI_Comm_rank(MPI_COMM_WORLD, &me);

	unsigned int n = 8;
	vector<int> a, b;
	a = { 1, 2, 3, 3, 6, 2, 7, 5 };
	b = { 2, 5, 3, 3, 7, 5, 8, 9 };

	if (me == 0) {
		send_work(a, b, nrProcs);
		vector <int> res(2 * n - 1);
		collect(nrProcs, res);
		for (int i = 0; i < res.size(); i++) {
			cout << res[i] << ' ';
		}
	}
	else {
		worker_karatsuba(me);
	}
	MPI_Finalize();
}