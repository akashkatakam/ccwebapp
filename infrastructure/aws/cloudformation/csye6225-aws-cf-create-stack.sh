#!/usr/bin/env bash

display_usage()
{
echo "Usage:$0 [StackName] [Availability_zone_1] [Availability_zone_2] [Availability_zone_3]"
}
if [[ $# -lt 4 ]];then
	display_usage
	exit 1
fi
echo "Creating Cloud Formation Stack $1....."
stackName=$1
region='us-east-1'
availabilityzone1=$region$2
availabilityzone2=$region$3
availabilityzone3=$region$4
stackID=$(aws cloudformation create-stack --stack-name $1 --template-body file://csye6225-cf-networking.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=Zone1,ParameterValue=$availabilityzone1 ParameterKey=Zone2,ParameterValue=$availabilityzone2 ParameterKey=Zone3,ParameterValue=$availabilityzone3 | grep StackId)
if [[ -z "$stackID" ]];then
	echo "Falied to create stack $1"
	exit 1
fi
status=$(aws cloudformation describe-stacks --stack-name  $1| grep StackStatus| cut -d'"' -f4)
while [[ "$status" != "CREATE_COMPLETE" ]]
do

       echo "$status"

       if [[ "$status" == "ROLLBACK_COMPLETE" ]];then
	       echo "$1 Stack_Create_Uncomplete !!"
	       exit 1
       fi

       sleep 4
       status=$(aws cloudformation describe-stacks --stack-name  $1 2>&1 | grep StackStatus| cut -d'"' -f4)

done

echo "Stack with name $1 creation completed successfully!!"

exit 0