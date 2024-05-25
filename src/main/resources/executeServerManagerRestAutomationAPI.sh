#Note: Script to execute REST call using curl command End to End flow
#Command to run : sh executeNFVRestCall.sh ngt.smrevamp.com 9080 10.151.1.165 4250 2 10.151.1.165 2828 8888
#Paramter explaination :: $1=hostname $2=hostport $3=serverIP $4=instanceport $5=servertype $6=copyfromIP $7=copyfromPort $8=utilityPort $9=SM Login username $10=SM Login Password

#function to extract response code from response data
getResponseCode(){
   echo $REST_CALL | cut -d ',' -f 2 | rev | cut -c 2- | rev | cut -d ':' -f 2
}

#RollBack Operation Perform
stopAndDeleteServerInstance(){
       
	   printf "\n===========================================================\n"
	   printf "Going To Perform << ROLLBACK OPERATION >>\n"
	   printf "=============================================================\n"
	   printf "\n\n"	
	   REST_CALL=$(curl --insecure -s \
		--header "Content-type:application/json" \
		--header "X-Username: $USERNAME" \
		--header "X-Password: $PASSWORD" \
		--request POST \
		--data '{"ipAddress": "'$1'", "port": "'$2'"}' \
		$STOP_SERVERINSTANCE)
		echo "STOP SERVER INSTANCE : $STOP_SERVERINSTANCE"
		echo "STOP_SERVER INSTANCE_CALL ::$REST_CALL"

		REST_CALL_RESPONSE_CODE=$(getResponseCode)
	        echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"

		if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
		    then
			   echo "STOP SERVER INSTANCE SUCCESS"
                           printf "\n\n"	
			   REST_CALL=$(curl --insecure -s \
			      --header "Content-type:application/json" \
			      --header "X-Username: $USERNAME" \
			      --header "X-Password: $PASSWORD" \
			      --request POST \
			      --data '{"utilityPort": "'$3'","ipAddress": "'$1'", "port": "'$2'"}' \
					$DELETE_SERVER_INS)

			   echo "DELETE SERVER INSTANCE : $DELETE_SERVER_INS"
			   echo "DLEETE_SERVER INSTANCE_CALL ::$REST_CALL"	

                           REST_CALL_RESPONSE_CODE=$(getResponseCode)
                           echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"

			   if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
			     then
				echo "DELETE SERVER INSTANCE SUCCESS"        
                           else
		               echo "DELETE SERVER INSTANCE FAILURE !!! Please Check catalina.out for more info."
                           fi
	       else
		          echo "STOP SERVER INSTANCE FAILURE !!! Please Check catalina.out for more info."
	       fi
	       return 1
}

#execute sequence of rest call
executeNFVRestCall(){

	# Start List of REST URL
	HTTP="https://"
	SM_NFV_PATH="ServerManager/api/nfv"
        LOGIN="$HTTP$1:$2/$SM_NFV_PATH/login"
	CREATE_SERVER="$HTTP$1:$2/$SM_NFV_PATH/addServer"
        CREATE_SERVER_INS="$HTTP$1:$2/$SM_NFV_PATH/addServerInstance"
	DELETE_SERVER_INS="$HTTP$1:$2/$SM_NFV_PATH/deleteServerInstance"
        DELETE_SERVER="$HTTP$1:$2/$SM_NFV_PATH/deleteServer" 
	COPY_CONFIGURATION="$HTTP$1:$2/$SM_NFV_PATH/copyServerInstance"
	SYNC_SERVERINSTANCE="$HTTP$1:$2/$SM_NFV_PATH/syncServerInstance"
	START_SERVERINSTANCE="$HTTP$1:$2/$SM_NFV_PATH/startServerInstance"
	STOP_SERVERINSTANCE="$HTTP$1:$2/$SM_NFV_PATH/stopServerInstance"
        RESTART_SERVERINSTANCE="$HTTP$1:$2/$SM_NFV_PATH/restartServerInstance"
	# End List of REST URL

	#print date & time in LOG file
	DATE_TIME=`date +'%d/%m/%Y %H:%M:%S:%3N'`
	printf "\n=============================================================\n"
	printf "executeNFVRestCall.sh Executed on :: $DATE_TIME\n"
	printf "=============================================================\n"
	
	#start executing rest calls
	REST_CALL=$(curl --insecure -s \
        --header "Content-type:application/json" \
        --header "X-Username: $USERNAME" \
        --header "X-Password: $PASSWORD" \
        --request POST \
         $LOGIN)

	echo "CHECKING LOGIN :: $LOGIN"
	echo "LOGIN_CALL_RESPONSE :: $REST_CALL"

	REST_CALL_RESPONSE_CODE=$(getResponseCode)
	echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"

	if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
	   then
			echo "LOGIN CALL SUCCESS"
			printf "\n\n" 
			REST_CALL=$(curl --insecure -s \
			--header "Content-type:application/json" \
			--header "X-Username: $USERNAME" \
			--header "X-Password: $PASSWORD" \
			--request POST \
			--data '{"utilityPort": "'$8'","serverType": "'$5'", "ipAddress": "'$3'"}' \
			 $CREATE_SERVER)

			echo "CREATING SERVER : $CREATE_SERVER"
			echo "CREATE_SERVER_CALL ::$REST_CALL"

			REST_CALL_RESPONSE_CODE=$(getResponseCode)
			echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"

			if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
			 then
				echo "CREATING SERVER SUCCESS"
				printf "\n\n" 
				REST_CALL=$(curl --insecure -s \
				--header "Content-type:application/json" \
				--header "X-Username: $USERNAME" \
				--header "X-Password: $PASSWORD" \
				--request POST \
				--data '{"utilityPort": "'$8'","serverType": "'$5'", "ipAddress": "'$3'", "port":"'$4'","copyFromIp": "'$6'", "copyFromPort": "'$7'"}' \
				 $CREATE_SERVER_INS)

				echo "CREATING SERVER_INSTANCE : $CREATE_SERVER_INS"
				echo "CREATE_SERVER_CALL ::$REST_CALL"

				REST_CALL_RESPONSE_CODE=$(getResponseCode)
				echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"

				if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
		                     then
					echo "CREATING SERVER INSTANCE SUCCESS"
					printf "\n\n" 
					REST_CALL=$(curl --insecure -s \
					--header "Content-type:application/json" \
					--header "X-Username: $USERNAME" \
					--header "X-Password: $PASSWORD" \
					--request POST \
					--data '{"ipAddress": "'$3'", "port": "'$4'"}' \
					 $START_SERVERINSTANCE)

					echo "STARTING SERVER : $START_SERVERINSTANCE"
					echo "START_SERVERINSTANCE_CALL ::$REST_CALL"

				        REST_CALL_RESPONSE_CODE=$(getResponseCode)
					echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"
					if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
				 	  then
					  echo "START SERVER SUCCESS"
					  printf "\n\n" 
					  REST_CALL=$(curl --insecure -s \
						--header "Content-type:application/json" \
						--header "X-Username: $USERNAME" \
						--header "X-Password: $PASSWORD" \
						--request POST \
						--data '{"copyFromIp": "'$6'", "copyFromPort": "'$7'", "copyToIp":"'$3'","copyToPort": "'$4'"}' \
						 $COPY_CONFIGURATION)

						echo "COPY CONFIGURATION : $COPY_CONFIGURATION"
						echo "COPY CONFIGURATION_CALL ::$REST_CALL"

						REST_CALL_RESPONSE_CODE=$(getResponseCode)
						echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"
						if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
						 then
							echo "COPY CONFIGURATION SUCCESS"
							printf "\n\n" 
							REST_CALL=$(curl --insecure -s \
							--header "Content-type:application/json" \
							--header "X-Username: $USERNAME" \
							--header "X-Password: $PASSWORD" \
							--request POST \
							--data '{"ipAddress": "'$3'", "port": "'$4'"}' \
							 $SYNC_SERVERINSTANCE)

							echo "SYNC SERVER INSTANCE : $SYNC_SERVERINSTANCE"
							echo "SYNC SERVER INSTANCE_CALL ::$REST_CALL"

							REST_CALL_RESPONSE_CODE=$(getResponseCode)
							echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"
			
							if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
							  then
							     echo "SYNC SERVER INSTANCE SUCCESS"
								printf "\n\n" 
								REST_CALL=$(curl --insecure -s \
								--header "Content-type:application/json" \
								--header "X-Username: $USERNAME" \
								--header "X-Password: $PASSWORD" \
								--request POST \
								--data '{"ipAddress": "'$3'", "port": "'$4'"}' \
								 $RESTART_SERVERINSTANCE)

								echo "RESTART SERVER INSTANCE : $RESTART_SERVERINSTANCE"
								echo "RESTART_SERVERINSTANCE_CALL ::$REST_CALL"

								REST_CALL_RESPONSE_CODE=$(getResponseCode)
								echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE" 	
								 if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
								  then
								     echo "RESTART SERVER INSTANCE SUCCESS"
								     sleep 40s	
							             printf "\n\n" 
									REST_CALL=$(curl --insecure -s \
									--header "Content-type:application/json" \
									--header "X-Username: $USERNAME" \
									--header "X-Password: $PASSWORD" \
									--request POST \
									--data '{"ipAddress": "'$3'", "port": "'$4'"}' \
									 $STOP_SERVERINSTANCE)
									sleep 40s
									echo "STOP SERVER INSTANCE : $STOP_SERVERINSTANCE"
									echo "STOP_SERVER INSTANCE_CALL ::$REST_CALL"
									REST_CALL_RESPONSE_CODE=$(getResponseCode)
echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"
									  if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
									   then
									     echo "STOP SERVER INSTANCE SUCCESS"
									     printf "\n\n"	
									     REST_CALL=$(curl --insecure -s \
										--header "Content-type:application/json" \
										--header "X-Username: $USERNAME" \
										--header "X-Password: $PASSWORD" \
										--request POST \
										--data '{"utilityPort": "'$8'","ipAddress": "'$3'", "port": "'$4'"}' \
										 $DELETE_SERVER_INS)

										echo "DELETE SERVER INSTANCE : $DELETE_SERVER_INS"
										echo "DLEETE_SERVER INSTANCE_CALL ::$REST_CALL"	
                                                                                
                                                                                 
										REST_CALL_RESPONSE_CODE=$(getResponseCode)
echo "REST_CALL_RESPONSE_CODE :: $REST_CALL_RESPONSE_CODE"
																								
									 	if [ $REST_CALL_RESPONSE_CODE = "\"1"\" ]
										  then
										  echo "DELETE SERVER INSTANCE SUCCESS"
									        else
										  echo "DELETE SERVER INSTANCE FAILURE"
									        fi    
								         else
								             echo "STOP SERVER INSTANCE FAILURE"
							                 fi 			
							else
						              echo "RESTART SERVER INSTANCE FAILURE"
							      stopAndDeleteServerInstance $ipAddress $instancePort $utilityPort >> executeServerManagerRestAutomationAPI_$DATETIME.out
                                                               return 1	
							fi 	
						else
						    echo "SYNC SERVER INSTANCE FAILURE"
						     stopAndDeleteServerInstance $ipAddress $instancePort $utilityPort >> executeServerManagerRestAutomationAPI_$DATETIME.out
                                                     return 1
						fi
					else
					    echo "COPY CONFIGURATION FAILURE"
					    stopAndDeleteServerInstance $ipAddress $instancePort $utilityPort >> executeServerManagerRestAutomationAPI_$DATETIME.out
                                            return 1
					fi						
				else
 				  echo "START SERVER FAILURE"
                                  stopAndDeleteServerInstance $ipAddress $instancePort $utilityPort >> executeServerManagerRestAutomationAPI_$DATETIME.out
                                  return 1
				fi
			
			  else
		           echo "CREATING SERVER INSTANCE FAILURE"
		          fi
              else
                   echo "CREATING SERVER FAILURE"
              fi
		
	else
	 echo "LOGIN CALL FAILURE"
	fi
}

#calling function
printf "\n 1) Find executeServerManagerRestAutomationAPI.out for LOGS\n" 
printf "\n 2) Command to Run : sh executeNFVRestCall.sh ngt.smrevamp.com 9080 10.151.1.165 4250 2 10.151.1.165 2828 8888 admin adminkk \n"
printf "\n 3) Paramter Explaination :: "'$1'"=hostname "'$2'"=hostport "'$3'"=serverIP "'$4'"=instanceport "'$5'"=servertype "'$6'"=copyfromIP "'$7'"=copyfromPort "'$8'"=utilityPort "'$9'"=SM Login Username "' $10'"=SM Login Password \n"
DATE=`date +'%d/%m/%Y_'`
TIME=`date +'%H:%M:%S:%3N'`
DATETIME="`date '+%Y%m%d%H%M%S'`"
USERNAME=$9
PASSWORD=${10}
ipAddress=$3
instancePort=$4
utilityPort=$8

#check for logfile backp folder
if [ -d "RestCallLogBkp" ] 
 then
  echo "RestCallLogBkp folder exits to backup log files"
else
    mkdir RestCallLogBkp
fi

count=`ls -l | grep 'executeServerManagerRestAutomationAPI_*.*.out' | wc -l`
if [ $count -gt 0 ]
  then
    mv executeServerManagerRestAutomationAPI*.out RestCallLogBkp
else
   echo ""
fi
executeNFVRestCall $1 $2 $3 $4 $5 $6 $7 $8 $9 $10 >> executeServerManagerRestAutomationAPI_$DATETIME.out
