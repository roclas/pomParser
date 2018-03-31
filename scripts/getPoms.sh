#!/usr/bin/env bash

fetchJarPathsFromDependencyFile () {
    for i in $(cat $1 | grep "class=.INeed.>" | sed "s/[^>]*>//" | sed "s/<.*//")
    do
    coursier fetch $i
    done | grep jar 
}


extractPomsFromJars () {
    mkdir -p /tmp/poms
    n=0
	for i in $(find $1 -name "*jar"|sort)
	do
        n=$(( n + 1 ))
		internalFile=$(jar -tf $i | grep -i "pom.xml")
		if [ ! -z "$internalFile" ]
		then
		    echo "$internalFile"
		    unzip -p $i $internalFile> /tmp/poms/$n.pom.xml
		fi
	done
	mv /tmp/poms $2
}


getPoms () {
if [ "$#" -lt 2 ]; then
    echo "Illegal number of parameters"
    echo "Usage:"
    echo "	$0 <source_dir_to_search_for_poms> <destination_dir_for_effective_poms>"
    exit -1
fi

n=0
extractPomsFromJars $1 $1
for i in $(find $1 -name pom.xml)
do
 n=$(( n + 1 ))
 mvn help:effective-pom -f $i|awk '{\
		if($0 ~ /<\/?project[ >]/){\
			print $0;\
			a=!a;n++\
		}else{if(a>0){\
			print $0;\
		}}\
	}\
' > $2/$n.xml
done
}

