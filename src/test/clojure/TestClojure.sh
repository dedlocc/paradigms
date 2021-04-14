#!/bin/bash
set -euo pipefail

if [[ -z "$1" ]] ; then
    echo Usage: $(basename "$0") [test-class] [variant]
    exit 1
fi

rpath() {
    [[ $1 = /* ]] && echo "$1" || echo "$PWD/${1#./}"
}

CLASS="$1"
CLOJURE="$(dirname $(rpath "$0"))"
OUT=__OUT
REPO="$(dirname "$CLOJURE")"
LIB="$CLOJURE/lib/*"

javac \
    -d "$OUT" \
    "--class-path=$LIB:$REPO/java:$REPO/javascript:$REPO/clojure" \
    "$CLOJURE/${CLASS//\.//}.java" \
 && java -ea "--class-path=$LIB:$OUT" "$CLASS" "${2-}"
