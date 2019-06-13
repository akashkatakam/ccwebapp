# Script to delete networking resources using AWS CLI

set -e

echo "Enter the name of the VPC you would like to delete-"
read vpc_name

VpcName=$vpc_name

VpcId=$(aws ec2 describe-vpcs --filter "Name=tag:Name,Values=$VpcName"|grep -m 1 VpcId|cut -d'"' -f4)
if [ -z "$VpcId" ];then
    echo "Failed to fetch VPC ID."
    exit 1
fi
echo "Fetched VPC with ID: "$VpcId


echo "Fetching subnets..."
subnet_1=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[0]'|grep SubnetId|cut -d'"' -f4)
if [ -z "$subnet_1" ];then
    echo "Failed to fetch subnet 1."
    exit 1
fi
echo "Fetched Subnet 1 with Id: "$subnet_1

subnet_2=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[1]'|grep SubnetId|cut -d'"' -f4)
if [ -z "$subnet_2" ];then
    echo "Failed to fetch subnet 2."
    exit 1
fi
echo "Fetched Subnet 2 with Id: "$subnet_2

subnet_3=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[2]'|grep SubnetId|cut -d'"' -f4)
if [ -z "$subnet_3" ];then
    echo "Failed to fetch subnet 3."
    exit 1
fi
echo "Fetched Subnet 3 with Id: "$subnet_3


echo "Fetching gateways..."
InternetGatewayId=$(aws ec2 describe-internet-gateways --filter "Name=attachment.vpc-id,Values=$VpcId"|grep InternetGatewayId|cut -d'"' -f4)
if [ -z "$InternetGatewayId" ];then
    echo "Failed to fetch Internet Gateway."
    exit 1
fi
echo "Fetched Internet Gateway with ID: "$InternetGatewayId


echo "Fetching route table..."
RouteTableId=$(aws ec2 describe-route-tables --filter "Name=route.gateway-id,Values=$InternetGatewayId"|grep -m 1 RouteTableId|cut -d'"' -f4)
if [ -z "$RouteTableId" ];then
    echo "Failed to fetch Route Table."
    exit 1
fi
echo "Fetched Route Table with ID: "$RouteTableId


echo "Deleting public gateway route from route table"
aws ec2 delete-route --route-table-id $RouteTableId --destination-cidr-block 0.0.0.0/0

echo "Detaching Internet Gateway from VPC......"
aws ec2 detach-internet-gateway --internet-gateway-id $InternetGatewayId --vpc-id $VpcId


echo "Deleting Internet Gateway........."
aws ec2 delete-internet-gateway --internet-gateway-id $InternetGatewayId


echo "Deleting subnets........"
aws ec2 delete-subnet --subnet-id $subnet_1
aws ec2 delete-subnet --subnet-id $subnet_2
aws ec2 delete-subnet --subnet-id $subnet_3

echo "Deleting Route Table......."
aws ec2 delete-route-table --route-table-id $RouteTableId

echo "Deleting VPC......"
aws ec2 delete-vpc --vpc-id $VpcId

echo "Delete completed Successfully!"



