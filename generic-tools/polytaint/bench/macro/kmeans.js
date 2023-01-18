
function getData(n) {
    var data = new Array(n);
    console.log(">>>>>>> generating: ",n," kmeans data points >>>>>>");

    for (let i = 0; i < n; i++) {
        for (let j = 0; j < 3; j++) {
            var a = randomBetween(-1000, 1000);           
            var b = randomBetween(-1000, 1000);
            var c = randomBetween(-1000, 1000);
            var temp = [a, b, c];
            data[i] = temp;
        }
    }

    var max_a = 0; var min_a = 0;
    var max_b = 0; var min_b = 0;
    var max_c = 0; var min_c = 0;

    for (var i = 0; i < n; i++) {
        max_a = (data[i][0] > max_a) ? data[i][0] : max_a;
        max_b = (data[i][1] > max_b) ? data[i][1] : max_b;
        max_c = (data[i][2] > max_c) ? data[i][2] : max_c;


        min_a = (data[i][0] < min_a) ? data[i][0] : min_a;
        min_b = (data[i][1] < min_b) ? data[i][1] : min_b;
        min_c = (data[i][2] < min_c) ? data[i][2] : min_c;

    }

    for (var i = 0; i < n; i++) {
        data[i][0] = (data[i][0] - min_a) / (max_a - min_a);
        data[i][1] = (data[i][1] - min_b) / (max_b - min_b);
        data[i][2] = (data[i][2] - min_c) / (max_c - min_c);
    }

    console.log(">>>>>>> data generation complete >>>>>");

    return data;
}

function randomBetween(min, max) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    return Math.floor(
        Math.random() * (max - min) + min
    );
}

function calcMeanCentroid(dataSet, start, end) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    const features = dataSet[0].length;
    const n = end - start;
    let mean = [];
    for (let i = 0; i < features; i++) {
        mean.push(0);
    }
    for (let i = start; i < end; i++) {
        for (let j = 0; j < features; j++) {
            mean[j] = mean[j] + dataSet[i][j] / n;
        }
    }
    return mean;
}

function getRandomCentroidsNaiveSharding(dataset, k) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    const numSamples = dataset.length;

    const step = Math.floor(numSamples / k);
    const centroids = [];
    for (let i = 0; i < k; i++) {
        const start = step * i;
        let end = step * (i + 1);
        if (i + 1 === k) {
            end = numSamples;
        }
        centroids.push(calcMeanCentroid(dataset, start, end));
    }
    return centroids;
}

function getRandomCentroids(dataset, k) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    const numSamples = dataset.length;
    const centroidsIndex = [];
    let index;
    while (centroidsIndex.length < k) {
        index = randomBetween(0, numSamples);
        if (centroidsIndex.indexOf(index) === -1) {
            centroidsIndex.push(index);
        }
    }
    const centroids = [];
    for (let i = 0; i < centroidsIndex.length; i++) {
        const centroid = [...dataset[centroidsIndex[i]]];
        centroids.push(centroid);
    }
    return centroids;
}

function compareCentroids(a, b) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    for (let i = 0; i < a.length; i++) {
        if (a[i] !== b[i]) {
            return false;
        }
    }
    return true;
}

function shouldStop(oldCentroids, centroids, iterations) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    const MAX_ITERATIONS = 50;
    if (iterations > MAX_ITERATIONS) {
        return true;
    }
    if (!oldCentroids || !oldCentroids.length) {
        return false;
    }
    let sameCount = true;
    for (let i = 0; i < centroids.length; i++) {
        if (!compareCentroids(centroids[i], oldCentroids[i])) {
            sameCount = false;
        }
    }
    return sameCount;
}


function getDistanceSQ(a, b) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    const diffs = [];
    for (let i = 0; i < a.length; i++) {
        diffs.push(a[i] - b[i]);
    }
    return diffs.reduce((r, e) => (r + (e * e)), 0);
}


function getLabels(dataSet, centroids) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    const labels = {};
    for (let c = 0; c < centroids.length; c++) {
        labels[c] = {
            points: [],
            centroid: centroids[c],
        };
    }

    for (let i = 0; i < dataSet.length; i++) {
        const a = dataSet[i];
        let closestCentroid, closestCentroidIndex, prevDistance;
        for (let j = 0; j < centroids.length; j++) {
            let centroid = centroids[j];
            if (j === 0) {
                closestCentroid = centroid;
                closestCentroidIndex = j;
                prevDistance = getDistanceSQ(a, closestCentroid);
            } else {

                const distance = getDistanceSQ(a, centroid);
                if (distance < prevDistance) {
                    prevDistance = distance;
                    closestCentroid = centroid;
                    closestCentroidIndex = j;
                }
            }
        }

        labels[closestCentroidIndex].points.push(a);
    }

    return labels;
}

function getPointsMean(pointList) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    const totalPoints = pointList.length;
    const means = [];
    for (let j = 0; j < pointList[0].length; j++) {
        means.push(0);
    }
    for (let i = 0; i < pointList.length; i++) {
        const point = pointList[i];
        for (let j = 0; j < point.length; j++) {
            const val = point[j];
            means[j] = means[j] + val / totalPoints;
        }
    }
    return means;
}

function recalculateCentroids(dataSet, labels, k) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    let newCentroid;
    const newCentroidList = [];
    for (const k in labels) {
        const centroidGroup = labels[k];
        if (centroidGroup.points.length > 0) {

            newCentroid = getPointsMean(centroidGroup.points);
        } else {

            newCentroid = getRandomCentroids(dataSet, 1)[0];
        }
        newCentroidList.push(newCentroid);
    }
    return newCentroidList;
}

function kmeans(dataset, k, useNaiveSharding = true) {
    var sec_int = Polyglot.eval("secL", "sInt(0)");
    const MAX_ITERATIONS = 50;
    if (dataset.length && dataset[0].length && dataset.length > k) {

        let iterations = 0;
        let oldCentroids, labels, centroids;


        if (useNaiveSharding) {
            centroids = getRandomCentroidsNaiveSharding(dataset, k);
        } else {
            centroids = getRandomCentroids(dataset, k);
        }


        while (!shouldStop(oldCentroids, centroids, iterations)) {

            oldCentroids = [...centroids];
            iterations++;


            labels = getLabels(dataset, centroids);
            centroids = recalculateCentroids(dataset, labels, k);

        }

        const clusters = [];
        for (let i = 0; i < k; i++) {
            clusters.push(labels[i]);
        }
        const results = {
            clusters: clusters,
            centroids: centroids,
            iterations: iterations,
            converged: iterations <= MAX_ITERATIONS,
        };
        return results;
    } else {
        console.log('Invalid dataset');
    }
}

function printData(info) {
    for (let i = 0; i < info.length; i++) {
        console.log("[");
        for (let j = 0; j < 3; j++) {
            console.log(info[i][j], ",");

        }
        console.log("]");

    }
}


function doKmeans(){
    var start_data = new Date();
    var testData = Polyglot.eval("secL", "sInt(0)");
    testData = getData(4);
    console.log("testData: ",testData);
    var stop_data = new Date();
    
    var diff_data = stop_data - start_data;
    console.log("kmeans data size: ", testData.length, " generation time = ", diff_data, " ms");
    
    
    
    var start = new Date();
    var result = kmeans(testData, 2);
    var stop = new Date();
    var diff = stop - start;
    
    var total = diff_data + diff;
    console.log("kmeans done: run time = ", diff, " ms");
    console.log("kmeans total time (data + kmeans) = ", total, " ms");
}

doKmeans();




