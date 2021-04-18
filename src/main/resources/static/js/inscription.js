
function showtext() {
  var checkBox1 = document.getElementById("cpaypal");
  var text1 = document.getElementById("spaypal");
  if (checkBox1.checked === true) {
    text1.style.display = "block";
  } else {
    text1.style.display = "none";
  }

  var checkBox2 = document.getElementById("cpinterest");
  var text2 = document.getElementById("spinterest");
  if (checkBox2.checked === true) {
    text2.style.display = "block";
  } else {
    text2.style.display = "none";
  }

  var checkBox3 = document.getElementById("cfacebook");
  var text3 = document.getElementById("sfacebook");
  if (checkBox3.checked === true) {
    text3.style.display = "block";
  } else {
    text3.style.display = "none";
  }

  var checkBox4 = document.getElementById("ctwitter");
  var text4 = document.getElementById("stwitter");
  if (checkBox4.checked === true) {
    text4.style.display = "block";
  } else {
    text4.style.display = "none";
  }

  var checkBox5 = document.getElementById("cinstagram");
  var text5 = document.getElementById("sinstagram");
  if (checkBox5.checked === true) {
    text5.style.display = "block";
  } else {
    text5.style.display = "none";
  }

  var checkBox6 = document.getElementById("cgoogle");
  var text6 = document.getElementById("sgoogle");
  if (checkBox6.checked === true) {
    text6.style.display = "block";
  } else {
    text6.style.display = "none";
  }
}
