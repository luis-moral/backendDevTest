# Notes
- If one or more product ids fail when retrieving the product details, those products will be removed from the list and it will just return the valid ones.
- @Cacheable does not seem to work with reactive streams, so I added the caches manually

# Performance
- Product details requests run in different threads simultaneously, up to `service.similar-products.parallelism` threads.
- Supposing that product updates can be delayed a few seconds, I added an after write, 60 seconds, similar ids and product details cache (`repository.product.cache.duration`).
- Added a circuit breaker just in case the remote service could be unavailable or too busy to process the requests.