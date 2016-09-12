echo "Making and publishing a file to itch"
echo "Starting the build script"

ant -q

echo "Finished building"
echo "Renaming file to itch sutable one"
cp build/jar/Painter*.jar ~/Documents/Butler/Painter.jar
echo "Starting buttler for pushing"
cd ~/Documents/Butler
mkdir build
mv Painter.jar build/Painter.jar
./butler push build  h2n0/spriter:windows-linux-osx
rm -rf build
echo "Finished pushing with butler"
echo "Finished the publishing process"
