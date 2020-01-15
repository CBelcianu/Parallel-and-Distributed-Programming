### Kubernetes with Google Cloud Platform Kubernetes Engine (GKE)



##### 1. CREATING A NEW CLUSTER

We'll use GKE to create a Kubernetes cluster on Google's cloud platform. Navigate to the 'Clusters' tab of GKE and hit 'CREATE CLUSTER' The cluster will have 1 node and will run on a VM with 4vCores.

![]( https://github.com/CBelcianu/Parallel-and-Distributed-Programming/blob/master/tiv/images/newCluster.PNG )

Navigating back we should see that our cluster has been created.

![](https://github.com/CBelcianu/Parallel-and-Distributed-Programming/blob/master/tiv/images/allClusters.PNG)

We can connect to our cluster by hitting 'connect' and then 'Run in cloud shell'.



##### 2. SETTING UP THE CLUSTER

Run the following command to connect to your cluster. [PROJECT_ID] represents the id of your Cloud Platform Project associated to your cluster.

```shell
$ gcloud container clusters get-credentials pdp-cluster --zone us-central1-a --project [PROJECT_ID]
```

Running the following command should result in:

```shell
$ kubectl get services
NAME                TYPE           CLUSTER-IP    EXTERNAL-IP      PORT(S)          AGE
kubernetes          ClusterIP      10.0.0.1      <none>           443/TCP          21s
```

Good, looks like the cluster is working and we can start adding some services. We'll add the invoices, expected-date, authentication and ambassador services. In order to apply invoices, expected-date and authentication we have to use Docker.

Run the following commands to build the docker images corresponding to each of our services:

```shell
$ docker build ../invoices_svc/ -t invoices_svc:v4
$ docker build ../expected_date_svc/ -t expected_date_svc:v1
$ docker build ../auth_svc/ -t auth_svc:v1
```

Keep in mind that the commands were executed from inside the 'kube' directory. Good, now, to make sure that Docker actually built the images we shall run the following command:

```shell
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
invoices_svc        v4                  809c8255e1b8        30s ago             919MB
expected_date_svc   v1                  923c74w938s8        27s ago             756MB
auth_svc            v1                  2934u389ds32        20s ago             539MB
```

Hooray! We've successfully built the images, but what now? Pretty simple actually, now we have to push the into the Google Container Registry (GCR) associated with our project. In order to push our images, we have to tell docker to "gcr.io/[PROJECT_ID]/[IMAGE]:[TAG]" where [PROJECT_ID] you already know what it is and [IMAGE] is the name of the image (the string under 'REPOSITORY' in the above output) and [TAG] is it's tag.

In our case, we should run these commands:

```shell
$ docker tag invoices_svc:v4 "gcr.io/${PROJECT_ID}/invoices_svc:v4"
$ docker tag expected_date_svc:v1 "gcr.io/${PROJECT_ID}/expected_date_svc:v1"
$ docker tag auth_svc:v1 "gcr.io/${PROJECT_ID}/auth_svc:v1"
```

Keep in mind the PROJECT_ID is the id of the project associated with the cluster. Mine was 'pdpkube' for example.

Now, to push the images to GCR just run these commands:

```shell
$ docker push "gcr.io/${PROJECT_ID}/invoices_svc:v4"
$ docker push "gcr.io/${PROJECT_ID}/expected_date_svc:v1"
$ docker push "gcr.io/${PROJECT_ID}/auth_svc:v1"
```

Let's see if it worked. Navigate to your GCR and you should see something like this:

![]( https://github.com/CBelcianu/Parallel-and-Distributed-Programming/blob/master/tiv/images/images.PNG )

I messed up a little bit and pushed the invoices image twice but it won't affect us :smile:.

##### 3. ADDING SERVICES TO THE CLUSTER

We are making quite the progress, but so far we don't have any of the services up an running, so let's get to it.

If we run `$ ls` In the 'kube' directory we should see the following files:

```shell
ambassador.yaml  auth_svc.yaml  expected_date_svc.yaml  invoices_svc.yaml
```

Now we have to tell each .yaml file, besides the ambassador one, where to find the docker image. In order to do this execute `$ vim invoices_svc.yaml`.

```yaml
template:
    metadata:
      labels:
        run: invoices-svc
    spec:
      containers:
      - image: gcr.io/[PROJECT_ID]/invoices_svc:v4 #THIS IS WHERE WE SPECIFIED THE PATH
        imagePullPolicy: IfNotPresent
        name: invoices-svc
        env:
          - name: EXPECTED_DATE_URI
            value: http://expected-date-svc.default.svc.cluster.local
        ports:
        - containerPort: 8080
```

Now do the same for each .yaml file.