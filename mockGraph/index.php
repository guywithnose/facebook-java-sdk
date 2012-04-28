<?
usleep(100000);
fileLog();

if(isset($_SERVER['REQUEST_URI']))
{
  if($_SERVER['REQUEST_URI'] == "/restserver.php")
  {
    if(isset($_REQUEST['query']) && $_REQUEST['query'] == "SELECT name FROM user WHERE uid=4")
    {
      if(!isset($_REQUEST['access_token']))
        echo json_encode(array("data"=>array(array("name" => "Mark Zuckerberg")))); 
      elseif(isset($_REQUEST['access_token']) && $_REQUEST['access_token'] == "117743971608120|943716006e74d9b9283d4d5d8ab93204")
      {
        echo json_encode(array("data"=>array(array("name" => "Mark Zuckerberg")))); 
      }
      else
      {
        echo json_encode(array("error_code"=>190, "error_msg" => "Error validating access token")); 
      }
    }
  }
  elseif($_SERVER['REQUEST_URI'] == "/me")
  {
    if(!isset($_REQUEST['access_token']))
    {
      echo json_encode(array("id"=>0)); 
    }
    elseif(isset($_REQUEST['access_token']) && $_REQUEST['access_token'] == "206492729383450|2.N4RKywNPuHAey7CK56_wmg__.3600.1304560800.1-214707|6Q14AfpYi_XJB26aRQumouzJiGA")
    {
      echo json_encode(array("error"=>array("message"=>"Error invalidating access token:", "type"=>"OAuthException"))); 
    }
    elseif(isset($_REQUEST['access_token']) && $_REQUEST['access_token'] == "174236045938435|0073dce2d95c4a5c2922d1827ea0cca6")
    {
      echo json_encode(array("error"=>array("message"=>"An active access token must be used to query information about the current user.", "type"=>"invalid_request"))); 
    }
    else
    {
      echo json_encode(array("error"=>array("message"=>"Invalid OAuth access token.", "type"=>"OAuthException"))); 
    }
  }
  elseif($_SERVER['REQUEST_URI'] == "/jerry")
  {
    echo json_encode(array("id"=>"214707","name"=>"jerry guy","first_name"=>"jerry","last_name"=>"guy")); 
  }
  elseif($_SERVER['REQUEST_URI'] == "/117743971608120/insights")
  {
    if(isset($_REQUEST['access_token']) && $_REQUEST['access_token'] == "117743971608120|943716006e74d9b9283d4d5d8ab93204")
    {
      echo json_encode(array("error"=>array("message"=>"An access token is required to request this resource.", "type"=>"OAuthException"))); 
    }
  }
  elseif($_SERVER['REQUEST_URI'] == "/naitik")
  {
    if(isset($_REQUEST['access_token']) && $_REQUEST['access_token'] == "117743971608120|943716006e74d9b9283d4d5d8ab93204" && isset($_REQUEST['method']) && $_REQUEST['method'] == "DELETE")
    {
      echo json_encode(array("error"=>array("message"=>"(#200) User cannot access this application", "type"=>"OAuthException"))); 
    }
    elseif(isset($_REQUEST['access_token']) && $_REQUEST['access_token'] == "117743971608120|943716006e74d9b9283d4d5d8ab93204" && isset($_REQUEST['method']) && $_REQUEST['method'] == "GET")
      echo json_encode(array("id"=>5526183,"name" => "Mark Zuckerberg")); 
  }
  elseif($_SERVER['REQUEST_URI'] == "/daaku.shah")
  {
    if(isset($_REQUEST['access_token']) && $_REQUEST['access_token'] == "174236045938435|0073dce2d95c4a5c2922d1827ea0cca6")
    {
      echo json_encode(array("error"=>array("message"=>"An active access token must be used to query information about the current user.", "type"=>"invalid_request"))); 
    }
  }
}

function fileLog()
{
$fp = @fopen("log.txt", 'a');
if($fp)
{
  fwrite($fp, print_r($_REQUEST, true));
  fwrite($fp, "\n");
  fwrite($fp, print_r($_SERVER, true));
  fclose($fp);
}
}

?>
