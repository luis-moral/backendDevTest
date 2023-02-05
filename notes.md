# Notes
- If one or more product ids fail when retrieving the product details, those products will be removed from the list and it will just return the valid ones.


# Performance
- Product details requests run in different threads simultaneously, up to `service.similar-products.parallelism` threads.