#!/usr/bin/env bash
display_usage()
{
echo "Usage:$0 [StackName]"
}

if [[ $# -lt 1 ]];then
	display_usage
	exit 1
fi

echo "Deleting Stack $1 ......."
aws cloudformation delete-stack --stack-name $1

#Checking the status of the
status=$(aws cloudformation describe-stacks --stack-name $1 | grep StackStatus)

while [[ -n "$status" ]]
do
	echo "$status"
	sleep 3
	status=$(aws cloudformation describe-stacks --stack-name $1 2>&1 | grep StackStatus)
done

echo "Stack $1 deleted successfully!!"