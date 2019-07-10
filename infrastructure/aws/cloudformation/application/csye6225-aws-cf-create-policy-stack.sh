#!/usr/bin/env bash
#Script to create the Policy stack
display_usage()
{
echo "Usage:$0 [StackName]"
}
if [[ $# -lt 1 ]];then
	display_usage
	exit 1
fi

policyStackName=$1
echo "Policy stack name:"$policyStackName
domain=$(aws route53 list-hosted-zones --query HostedZones[0].Name --output text)
name=${domain::-1}
BucketName="${name}.csye6225.com"
echo ${BucketName}
StackID=$(aws cloudformation create-stack --stack-name $policyStackName --template-body file://csye6225-cf-policy-stack.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=S3Bucket,ParameterValue=${BucketName} |grep StackId)
if [[ $? -eq 0 ]]; then
    stackCompletion=$(aws cloudformation wait stack-create-complete --stack-name ${policyStackName})
        if [ $? -eq 0 ]; then
            echo "Stack with name $policyStackName creation completed successfully!!"
            exit 0
        else
            echo "Error in creating CloudFormation Stack"
        fi
else
    echo "Error in creating CloudFormation Stack"
    echo $StackID
    exit 1
fi