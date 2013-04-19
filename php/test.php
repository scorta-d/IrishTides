<?php

//$a="a b c d start <h1>Tides</h1><table>-table-</table>-end";
//$b=preg_split ("/[\s,]+/",$a);

/*
$myFile = "tides.html";
$fh = fopen($myFile, 'r');
$theData = fread($fh,65000);
fclose($fh);
print ("aha\n");
echo strlen($theData);
print ("\naha\n");
*/

$theData=file_get_contents ("http://www.irishtimes.com/weather/tides.html");

$a=$theData;

$b=preg_split("/<h1>Tides<\/h1>/", $a);

/*
foreach ($b as $item) {
   print ($item);
   print ("\n");
}
*/

$n=count($b);

if ($n > 0) {
   //print ("found:\n");
   $a=$b[1];
   //print ($a);
   //print ("\n");
   $b=preg_split("/<\/table>/", $a);
   
   //foreach ($b as $item) {
   //     print ($item);
   //      print ("\n");
   //}
   
   if (count($b) > 0) {
      $a=$b[0]."</table>";
   }
   
   $a=preg_replace('/width=".+?"/i',"",$a);
   $a=preg_replace('/class=".+?"/i',"",$a);
   $a=preg_replace('/cellpadding=".+?"/i',"",$a);
   
   
   print ($a);
   
}
?>