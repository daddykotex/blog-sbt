# Blog

This blog is built via [Laika](https://planet42.github.io/Laika/). The result is automatically pushed to https://github.com/daddykotex/blog-sbt-generated which is then deployed via netlify.

Note: GH pages is also enabled temporarely.

## Goals

This projet has multiple purposes.

The first one is to learn Laika, integrate it into my blogging workflow. I write mostly about Scala and when I write snippets of code in my post, I use [mdoc](https://scalameta.org/mdoc/) to compile them. My current blog is built via hugo. The integration with mdoc is non existent and manual.

The second one is to integrate mdoc into my blogging workflow so that my post are compiled if they need to be before they are rendered in HTML via Laika.


## Notes

It uses many sbt plugins:

* laika-sbt
* sbt-ghpages
* github-actions

### GH Pages

To allow `ghpages` to work correctly, we override the mappings to use `laikaSite / target` files.

### Netlify

We build the site via Github Actions, then we push it to another repository. This other repository is deployed via Netlify.

The repository is at: https://github.com/daddykotex/blog-sbt-generated