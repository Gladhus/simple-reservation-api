#!/bin/bash

CreateReservation() {
  curl  -o /dev/null -w "%{http_code} " --silent \
        --location --request POST 'localhost:8080/api/v1.1/reservation' \
        --header 'Content-Type: application/json' \
        --data-raw '{
            "email":"test@example.com",
            "fullName":"test fullname",
            "checkin": "'"$1"'",
            "checkout": "'"$2"'"
        }'
}

RunParallel() {
    CreateReservation $@ &
    CreateReservation $@ &
    CreateReservation $@ &
    CreateReservation $@ &

    wait  
}

ValidateResponseCodes() {
  local createdCounter=0
  declare -a responseCodes=($1)

  ## loop through response codes
  for i in "${responseCodes[@]}"
  do
    if [[ "$i" -eq "201" ]]; then
      let createdCounter++
    fi
  done

  if [[ "$createdCounter" -eq "1" ]]; then
    echo "Test passed : Only one reservation was created."
  elif [[ "$createdCounter" -eq "0" ]]; then
    echo "Error in test : No reservation was created."
  fi
}

Go() {

  local checkin="$1" checkout="$2"

  if [[ "$checkout" -eq "" ]]; then
    echo "go: invalid parameter(s) -- $checkin $checkout"
    echo "usage: go {checkin} {checkout}"
    return 1
  fi

  echo "Running test for dates checkin: $1 and checkout: $2"
  echo ""

  local result="$(RunParallel $@)"
  ValidateResponseCodes "$result"
}

Usage() {
  echo ""
  echo "  Reservation Concurrency Test                        2022-04-23 v1.00"
  echo ""
  echo "  Usage: test-concurrency.sh action"
  echo ""
  echo "  actions in:"
  echo ""
  echo "  go {checkin} {checkout}       Execute the test with the checkin and"
  echo "                                checkout dates provided as parameters."
  echo ""
  return 0
}


# Call the wanted action
case "$1" in
go)       shift ; Go "$@" ;;
*)        Usage ;;
esac

exit "$?"