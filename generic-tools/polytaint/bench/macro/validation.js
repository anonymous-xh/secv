
function validateInput(adjacencyList, personalised, damping, maxIteration, threshold) {
    if (maxIteration <= 0) {
        console.log("he maximum number of iterations must be greater than 0");
    }
    if (!Number.isInteger(maxIteration)) {
        console.log("The maximum number of iterations should be an integer");
    }
    if (threshold <= 0 || threshold >= 1) {

        console.log("The error threshold must be between 0 and 1 (exclusive).");
    }
    if (damping < 0 || damping > 1) {
        console.log("The damping factor must be between 0 and 1 (inclusive).");
    }

    var totalNodes = adjacencyList.length;
    for (let ls of adjacencyList) {
        for (let i of ls) {
            if (i < 0 || i >= totalNodes) {
                console.log("The adjacencylist can only have nodes with index 0 to ", totalNodes);
            }
        }
    }

    if (personalised) {
        var sumP = 0.0;
        for (let p of Object.keys(personalised)) {
            if (p < 0 || p >= totalNodes) {
                console.log("The personalised node must be an integer from 0 to ", totalNodes - 1, " not ", p);
            }
            sumP += personalised[p];
        }
        if (sumP > 1.0) {
            console.warn(`Summation of personalised vector (${sumP}) is greater than 1.`);
            for (let p of Object.keys(personalised)) {
                personalised[p] /= sumP;
            }
        }
    }
    return true;
}
