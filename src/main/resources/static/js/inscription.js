/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


document.getElementById("recherche").addEventListener("click", listeCommunes);
function listeCommunes() {
  let codeP = document.getElementById("lieuNaiss").value;
  const url = "https://geo.api.gouv.fr/communes?codePostal=" + codeP;
  const fetchOptions = { method: "GET" };
  fetch(url, fetchOptions)
    .then((response) => {
      return response.json();
    })
    .then((dataJSON) => {
      console.log(dataJSON);
      let communes = dataJSON;
      let texteHTML = "";
      for (let c of communes) {
        texteHTML += `<option th:value"${c.nom}">${c.nom}</option>`;
      }
      document.getElementById("commune").innerHTML = texteHTML;
    })
    .catch((error) => {
      console.log(error);
    });
}

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

document.addEventListener("click", renvoi);
function renvoi(event) {
  if (
    (event.pageY > 684 && event.pageY < 750) ||
    (event.pageY > 1322 && event.pageY < 1390) ||
    (event.pageY > 1993 && event.pageY < 2100)
  ) {
    if (event.pageX > 390 && event.pageX < 634) {
      console.log("renvoi1");
      window.scrollTo(0, 0);
    } else {
      if (event.pageX > 644 && event.pageX < 886) {
        console.log("renvoi2");
        window.scrollTo(0, 630);
      } else {
        if (event.pageX > 896 && event.pageX < 1138) {
          console.log("renvoi3");
          window.scrollTo(0, 1500);
        }
      }
    }
  }
}