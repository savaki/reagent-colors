package main

import (
	"github.com/codegangsta/cli"
	"log"
	"net/http"
	"os"
)

const (
	portFlag    = "port"
	docrootFlag = "docroot"
	pubFlag     = "pub"
	subFlag     = "sub"
)

func main() {
	app := cli.NewApp()
	app.Name = "server"
	app.Usage = "webserver for heroku"
	app.Version = "0.1"
	app.Flags = []cli.Flag{
		cli.StringFlag{portFlag, "8000", "the port to run on", "PORT"},
		cli.StringFlag{docrootFlag, "public", "where does content live", "DOCROOT"},
		cli.StringFlag{pubFlag, "", "url of pub js", "PUB_URL"},
		cli.StringFlag{subFlag, "", "url of sub js", "SUB_URL"},
	}
	app.Action = Run
	app.Run(os.Args)
}

func Run(c *cli.Context) {
	port := c.String(portFlag)
	docroot := c.String(docrootFlag)
	pub := c.String(pubFlag)
	sub := c.String(subFlag)

	routes := Routes(docroot, pub, sub)
	err := http.ListenAndServe(":"+port, routes)
	if err != nil {
		log.Fatalln(err)
	}
}
