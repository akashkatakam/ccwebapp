#!/usr/bin/env bash
set -e
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

status=$(aws cloudformation describe-stacks --stack-name $1 --output text)
if [[ -z "status" ]]; then
		echo "Stack does not exist"
	else
		terminateOutput=$(aws cloudformation delete-stack --stack-name $1)
		if [[ $? -eq 0 ]]; then
			echo "Deletion in Progress"
	    aws cloudformation wait stack-delete-complete --stack-name $1
	    echo "Deletion of stack successful"
	  else
	  echo "Deletion failed"
	  fi
fi
