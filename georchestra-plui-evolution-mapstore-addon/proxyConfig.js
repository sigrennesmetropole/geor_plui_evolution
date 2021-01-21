// this file contains configurations for dev proxy

const DEV_PROTOCOL = "http";
const DEV_HOST = "localhost:8080";

module.exports = {
    '/rest': {
        target: "https://dev.mapstore.geo-solutions.it/mapstore",
        secure: false,
        headers: {
            host: "dev.mapstore.geo-solutions.it"
        }
    },
    '/pdf': {
        target: "https://dev.mapstore.geo-solutions.it/mapstore",
        secure: false,
        headers: {
            host: "dev.mapstore.geo-solutions.it"
        }
    },
    '/mapstore/pdf': {
        target: "https://dev.mapstore.geo-solutions.it",
        secure: false,
        headers: {
            host: "dev.mapstore.geo-solutions.it"
        }
    },
    '/proxy': {
        target: "http://localhost:8082/",
        secure: false,
        headers: {
            host: "dev.mapstore.geo-solutions.it"
        }
    },
    '/docs': {
        target: "http://localhost:8081",
        pathRewrite: {'/docs': '/mapstore/docs'}
    },
    '/pluievolution': {
        target: "http://localhost:8082",
        pathRewrite: {'/pluievolution': '/'},
        headers: {
            host: "localhost"
        }   
    }
};