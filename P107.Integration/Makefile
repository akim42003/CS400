runApplication: compile
	java -cp .:../P106.Iterator App

runAllTests: compile
	java -jar ../junit5.jar -cp .:../P106.Iterator -c BackendTests

compile:
	javac -cp .:../P106.Iterator:../junit5.jar  *.java

clean:
	rm -f *.class
