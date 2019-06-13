# Script to delete a VPC

echo "Enter the name of the VPC you would like to delete-"
read vpc_name

VpcName=$vpc_name

VpcId=$(aws ec2 describe-vpcs --filter "Name=tag:Name,Values=$VpcName"|grep -m 1 VpcId|cut -d'"' -f4)
echo "VPC ID-"$VpcId

echo "Fetching subnets..."
subnet_1=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[0]'|grep SubnetId|cut -d'"' -f4)
subnet_2=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[1]'|grep SubnetId|cut -d'"' -f4)
subnet_3=$(aws ec2 describe-subnets --filter "Name=vpc-id,Values=$VpcId" --query 'Subnets[2]'|grep SubnetId|cut -d'"' -f4)
echo "Subnet 1-"$subnet_1 " || Subnet 2-"$subnet_2 " || Subnet 3-"$subnet_3

echo "Fetching gateways..."
InternetGatewayId=$(aws ec2 describe-internet-gateways --filter "Name=attachment.vpc-id,Values=$VpcId"|grep InternetGatewayId|cut -d'"' -f4)
echo "Gateway ID-"$InternetGatewayId

echo "Fetching route table..."
RouteTableId=$(aws ec2 describe-route-tables --filter "Name=route.gateway-id,Values=$InternetGatewayId"|grep -m 1 RouteTableId|cut -d'"' -f4)
echo "Route Table ID-"$RouteTableId
