# Build Atomic

# Build the documentation {#build-documentation}

---
**NOTE**

> For a **release**, the documentation will have to be built **first**, otherwise the product will not contain the latest auto-generated Eclipse Help files!

---

**Requirements**

- `pandoc` >= v1.19.2.1 on PATH
- `doxygen` >= v1.8.11 on PATH

## User documentation {#user-documentation}

To build the documentation, go to `{HOME}/docs/org.corpus_tools.atomic.doc/` and run 

    mvn clean package -P docs

The HTML documentation will be in `{HOME}/docs/org.corpus_tools.atomic.doc/target/docbkx/html/`.

The PDF documentation will be in `{HOME}/docs/org.corpus_tools.atomic.doc/src/main/resources/`.
It will also have been copied to `{HOME}/features/org.corpus_tools.atomic.feature/rootfiles/`.	

The Eclipse Help files generated by Doxygen will have been copied to `{HOME}/plugins/org.corpus_tools.atomic/doc/help/`.

## Developer documentation {#developer-documentation}

To build the documentation, go to `{HOME}/docs/org.corpus_tools.atomic.doc/` and run 

    mvn clean package -P docs

The HTML documentation will be in `{HOME}/docs/org.corpus_tools.atomic.doc/target/doxygen/`.