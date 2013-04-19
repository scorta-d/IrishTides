
a="scr2.png"
crop="-crop 320x480+0+28"
convert $a -scale 320x480 -bordercolor black  -border 0x100 -gravity center ${crop}  $a.png

file $a.png
xv  $a.png