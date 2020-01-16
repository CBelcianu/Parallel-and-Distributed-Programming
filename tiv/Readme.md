### Kubernetes with Google Cloud Platform Kubernetes Engine (GKE)



This cluster uses resources from [this medium tutorial]( https://medium.com/hackernoon/getting-started-with-microservices-and-kubernetes-76354312b556 ). You should get through it before reading this so you get a better idea of what's actually happening.

##### 1. CREATING A NEW CLUSTER

We'll use GKE to create a Kubernetes cluster on Google's cloud platform. Navigate to the 'Clusters' tab of GKE and hit 'CREATE CLUSTER'. The cluster will have 1 node and will run on a VM with 4vCores.

![]( https://github.com/CBelcianu/Parallel-and-Distributed-Programming/blob/master/tiv/images/newCluster.PNG )

Navigating back we should see that our cluster has been created.

![](https://github.com/CBelcianu/Parallel-and-Distributed-Programming/blob/master/tiv/images/allClusters.PNG)

We can connect to our cluster by hitting 'connect' and then 'Run in cloud shell'.



##### 2. SETTING UP THE CLUSTER

Run the following command to connect to your cluster. [PROJECT_ID] represents the id of your Cloud Platform Project associated to your cluster.

```shell
$ gcloud container clusters get-credentials pdp-cluster --zone us-central1-a --project [PROJECT_ID]
```

Running `$ kubectl get services` should result in:

```shell
NAME                TYPE           CLUSTER-IP    EXTERNAL-IP      PORT(S)          AGE
kubernetes          ClusterIP      10.0.0.1      <none>           443/TCP          21s
```

Good, looks like the cluster is working and we can start adding some services. We'll add the invoices, expected-date, authentication and ambassador services. In order to apply invoices, expected-date and authentication we have to use Docker in order to build images using thier associated Dockerfile.

Run the following commands to build the docker images corresponding to each of our services:

```shell
$ docker build ../invoices_svc/ -t invoices_svc:v4
$ docker build ../expected_date_svc/ -t expected_date_svc:v1
$ docker build ../auth_svc/ -t auth_svc:v1
```

Keep in mind that the commands were executed from inside the 'kube' directory (this is not mandatory, as long as you correctly specify the path to the Dockerfile you can run the commands from anywhere). Good, now to make sure that Docker actually built the images we shall execute `$ docker images`:

```shell
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
invoices_svc        v4                  809c8255e1b8        30s ago             919MB
expected_date_svc   v1                  923c74w938s8        27s ago             756MB
auth_svc            v1                  2934u389ds32        20s ago             539MB
```

Hooray! We've successfully built the images, but what now? Pretty simple actually, now we have to push them into the Google Container Registry (GCR) associated with our project. In order to push our images, we have to tell docker to tag the images to "gcr.io/[PROJECT_ID]/[IMAGE]:[TAG]" where [PROJECT_ID] you already know what it is and [IMAGE] is the name of the image (the string under 'REPOSITORY' in the above output) and [TAG] is it's tag.

In our case, we should run these commands:

```shell
$ docker tag invoices_svc:v4 "gcr.io/${PROJECT_ID}/invoices_svc:v4"
$ docker tag expected_date_svc:v1 "gcr.io/${PROJECT_ID}/expected_date_svc:v1"
$ docker tag auth_svc:v1 "gcr.io/${PROJECT_ID}/auth_svc:v1"
```

Keep in mind that PROJECT_ID is the id of the project associated with the cluster. Mine was 'pdpkube' for example.

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

Now we have to tell each .yaml file, besides the ambassador one, where to find their corresponding docker image. In order to do this execute `$ vim invoices_svc.yaml`.

```yaml
template:
    metadata:
      labels:
        run: invoices-svc
    spec:
      containers:
      - image: gcr.io/[PROJECT_ID]/invoices_svc:v4 #THIS IS WHERE WE SPECIFIED THE PATH
        imagePullPolicy: IfNotPresent #DON'T FORGET TO REPLACE [PROJECT_ID]
        name: invoices-svc
        env:
          - name: EXPECTED_DATE_URI
            value: http://expected-date-svc.default.svc.cluster.local
        ports:
        - containerPort: 8080
```

Now do the same for each .yaml file besides the ambassador one.

So what now? Well, just apply them  :trollface:.

```shell
$ kubectl apply -f invoices_svc.yaml
$ kubectl apply -f expected_date_svc.yaml
$ kubectl apply -f auth_svc.yaml
$ kubectl apply -f ambassador.yaml
```

Did it work? Let's check!

```shell
$ kubectl get services
NAME                TYPE           CLUSTER-IP    EXTERNAL-IP      PORT(S)          AGE
ambassador          LoadBalancer   10.0.10.202   35.222.203.242   80:32436/TCP     21h
ambassador-admin    NodePort       10.0.2.230    <none>           8877:30496/TCP   21h
auth-svc            ClusterIP      10.0.12.211   <none>           3000/TCP         21h
expected-date-svc   ClusterIP      10.0.14.177   <none>           80/TCP           21h
invoices-svc        ClusterIP      10.0.9.85     <none>           80/TCP           21h
kubernetes          ClusterIP      10.0.0.1      <none>           443/TCP          22h
```

It worked! Now we can run a couple of extra commands just to make sure.

```shell
$ kubectl get pods
NAME                                 READY   STATUS    RESTARTS   AGE
ambassador-68cbddb869-4ccmd          2/2     Running   1          21h
ambassador-68cbddb869-6qpgq          2/2     Running   0          21h
ambassador-68cbddb869-lnnvw          2/2     Running   0          21h
auth-svc-676c7bb6d-bhg9r             1/1     Running   0          21h
auth-svc-676c7bb6d-dvg6c             1/1     Running   0          21h
auth-svc-676c7bb6d-hltmn             1/1     Running   0          21h
expected-date-svc-58db79db6d-4kr6t   1/1     Running   0          21h
expected-date-svc-58db79db6d-5xpg4   1/1     Running   0          21h
expected-date-svc-58db79db6d-6z7z8   1/1     Running   0          21h
invoices-svc-89b875d48-dkjbp         1/1     Running   0          21h
invoices-svc-89b875d48-fvmc5         1/1     Running   0          21h
invoices-svc-89b875d48-rqf8m         1/1     Running   0          21h

$ kubectl get deployments
NAME                READY   UP-TO-DATE   AVAILABLE   AGE
ambassador          3/3     3            3           21h
auth-svc            3/3     3            3           21h
expected-date-svc   3/3     3            3           21h
invoices-svc        3/3     3            3           22h
```

Now all that remains is to test it. We will have to run a `curl` command to our ambassador service providing a authorization token. So how do we do that? Firstly we have to get the external IP address of our ambassador deployment. In GKE navigate to 'Services & Ingress'. You should see something like this:

![](https://github.com/CBelcianu/Parallel-and-Distributed-Programming/blob/master/tiv/images/services.PNG)

Under 'Endpoints' we can see the external IP address of the ambassador service. Click on it. It opens a new browser tab that says `{ok: false}`. That's because you didn't provide any token. Now on your local machine open a new terminal and execute the following command:

```shell
$ curl http://35.222.203.242/invoices/42 -H 'authorization: letmeinpleasekthxbye'
{"id":42,"ref":"INV-42","amount":4200,"balance":4190,"ccy":"GBP","expectedDate":"2020-01-23T15:46:09.892Z"}
```

It worked! Amazing, the server responded. Now, if you try to run the same command without specifying the token you should get `{ok: false}` again.
