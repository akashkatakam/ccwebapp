# Script to terminate the application stack

echo "Enter the application stack you want to delete:"
read stackName
stackname=$stackName

aws cloudformation delete-stack --stack-name $stackname

echo "Application stack deleted successfully"
