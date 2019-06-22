#Script to create the application stack

echo "Enter the application stack name:"
read stack_name

appStackName=$stack_name-app-$RANDOM

echo "Printing existing VPCs IDs..."
aws ec2 describe-vpcs | grep VpcId | cut -d'"' -f4

echo "Enter one VPC ID from above..."
read vpc_id
VpcId=$vpc_id

echo "Selected VPC ID-> "$VpcId

subnet_1=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[0]'|grep SubnetId|cut -d'"' -f4)
subnet_2=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[1]'|grep SubnetId|cut -d'"' -f4)
subnet_3=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[2]'|grep SubnetId|cut -d'"' -f4)

echo "Printing existing KeyPairs..."
aws ec2 describe-key-pairs | grep KeyName | cut -d'"' -f4
echo "Enter the KeyPair Name..."
read keypairname
keyPairName=$keypairname
echo "Selected key pair-> "$keyPairName

echo "Enter one AMI ID..."
read AmiId
amiId=$AmiId
echo "Selected AMI-> "$amiId



