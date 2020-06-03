package main

import (
	"fmt"
	"os/exec"
	"time"
)

func main() {
	go runQuery("http://localhost:8080/customers/1", 2)
	go runQuery("http://localhost:8081/orders?customerID=2", 3)
	select {}
}

func runQuery(url string, sleep int) {
	for {
		output, err := exec.Command("curl", url).Output()

		if err != nil {
			panic(err)
		}
		fmt.Println(string(output))
		time.Sleep(time.Second * time.Duration(sleep))
	}

}
