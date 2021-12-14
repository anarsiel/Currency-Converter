# Usage
currency1 & currency2: three-letter currency designations

`http://localhost:8080/convert?from={currency1}&to={currency2}`

# Benchmark testing

### Caching requests disabled
```
Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /convert?from=USD&to=RUB
Document Length:        69 bytes

Concurrency Level:      1
Time taken for tests:   33.788 seconds
Complete requests:      30
Failed requests:        0
Total transferred:      4890 bytes
HTML transferred:       2070 bytes
Requests per second:    0.89 [#/sec] (mean)
Time per request:       1126.255 [ms] (mean)
Time per request:       1126.255 [ms] (mean, across all concurrent requests)
Transfer rate:          0.14 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       1
Processing:   756 1126 389.4    996    2434
Waiting:      755 1125 389.3    996    2433
Total:        756 1126 389.5    997    2435

Percentage of the requests served within a certain time (ms)
  50%    997
  66%   1152
  75%   1278
  80%   1354
  90%   1530
  95%   2132
  98%   2435
  99%   2435
 100%   2435 (longest request)
```

### Caching requests enabled

```
Server Hostname:        localhost
Server Port:            8080

Document Path:          /convert?from=USD&to=RUB
Document Length:        69 bytes

Concurrency Level:      1
Time taken for tests:   2.432 seconds
Complete requests:      30
Failed requests:        0
Total transferred:      4890 bytes
HTML transferred:       2070 bytes
Requests per second:    12.33 [#/sec] (mean)
Time per request:       81.081 [ms] (mean)
Time per request:       81.081 [ms] (mean, across all concurrent requests)
Transfer rate:          1.96 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       0
Processing:     4   81 397.7      6    2186
Waiting:        4   80 397.5      5    2185
Total:          4   81 397.7      6    2187

Percentage of the requests served within a certain time (ms)
  50%      6
  66%      9
  75%     11
  80%     13
  90%     19
  95%     20
  98%   2187
  99%   2187
 100%   2187 (longest request)

```