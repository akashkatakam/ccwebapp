# Script to delete a VPC

echo "Enter the name of the VPC you would like to delete-"
read vpc_name

VpcName=$vpc_name-cloud-vpc

VpcId=$(aws ec2 describe-vpcs --filter "Name=tag:Name,Values=$VpcName"|grep -m 1 VpcId|cut -d'"' -f4)

echo "Enter subnet ID-"
subnet_1=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[0]'|grep SubnetId|cut -d'"' -f4)
subnet_2=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[1]'|grep SubnetId|cut -d'"' -f4)
subnet_3=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[2]'|grep SubnetId|cut -d'"' -f4)
subnet_4=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[3]'|grep SubnetId|cut -d'"' -f4)
subnet_5=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[4]'|grep SubnetId|cut -d'"' -f4)
subnet_6=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[5]'|grep SubnetId|cut -d'"' -f4)
