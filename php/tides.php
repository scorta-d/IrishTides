<?php

//$theData=file_get_contents ("http://www.irishtimes.com/weather/tides.html");
//$foo = system('wget http://www.irishtimes.com/weather/tides.html');
$theData=file_get_contents ("tides.html");
$a=$theData;

$b=preg_split("/<h1>Tides<\/h1>/", $a);

$n=count($b);

if ($n > 0) {
   $a=$b[1];
   $b=preg_split("/<\/table>/", $a);
   if (count($b) > 0) {
      $a=$b[0]."</table>";
   }



   $b=preg_split("/<\/th><\/tr>/",$a);
   if (count($b) > 2) {
      $tim=time();
      $a='<html><head><title>'.date('Ymd H:i:s',filemtime('tides.html')).'</title></head><body><table border="1">'.$b[2].'</body></html>';
   }


   $a=preg_replace('/width=".+?"/i',"",$a);
   $a=preg_replace('/class=".+?"/i',"",$a);
   $a=preg_replace('/cellpadding=".+?"/i',"",$a);
   $a=preg_replace('/cellspacing=".+?"/i',"",$a);
   $a=preg_replace('/summary=".+?"/i',"",$a);
   $a=preg_replace('/<td ><\/td>/',"<td>&nbsp;</td>",$a);
   

   
   print ($a);
   
}
?>
