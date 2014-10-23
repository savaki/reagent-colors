package main

import (
	"github.com/gin-gonic/gin"
	"html/template"
	"net/http"
)

func Routes(docroot, pubUrl, subUrl string) http.Handler {
	routes := gin.New()

	data := struct {
		Pub string
		Sub string
	}{
		Pub: pubUrl,
		Sub: subUrl,
	}

	index := template.Must(template.ParseFiles(docroot + "/index.html"))
	pub := template.Must(template.ParseFiles(docroot + "/pub.tmpl"))
	sub := template.Must(template.ParseFiles(docroot + "/sub.tmpl"))

	routes.GET("/", func(c *gin.Context) {
		index.Execute(c.Writer, data)
	})
	routes.GET("/pub.html", func(c *gin.Context) {
		pub.Execute(c.Writer, data)
	})
	routes.GET("/sub.html", func(c *gin.Context) {
		sub.Execute(c.Writer, data)
	})
	routes.Static("stylesheets", docroot+"/stylesheets")
	return routes
}
