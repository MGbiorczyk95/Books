function readBooks(selectedValue){
    httpGetAsync("http://localhost:8080/api/category/" + selectedValue + "/books").then(function(result){
      var response = JSON.parse(result);
      var numOfBooks = response.length;
      if(numOfBooks!=0){
      var titles = [];
      var ratings = [];
      var publicatedDates = [];
      var thumbnails = [];
      for(var i=0;i<numOfBooks;i++){
        titles[i] = response[i].title;

        if(response[i].averageRating!=0)
          ratings[i] = response[i].averageRating;
        else
          ratings[i] = "-";

        publicatedDates[i] = getFormatedDate(response[i].publishedDate);

        if(response[i].thumbnailUrl!=null)
          thumbnails[i] = response[i].thumbnailUrl;
        else
          thumbnails[i] = "https://nnp.wustl.edu/img/bookCovers/genericBookCover.jpg";
      }
      createDivs(numOfBooks);
      createImages(numOfBooks,thumbnails);
      createBookInfo(numOfBooks,titles,ratings,publicatedDates);
      }
      else {
        document.getElementById("booksBox").innerHTML = "<h1>No results<h1>";
      }
    });
};
function createDivs(numberOfDivs){
    var booksBox = document.getElementById("booksBox");
    booksBox.innerHTML = "";
    var i=0;
    while(i<numberOfDivs)
    {
        if(!document.getElementById('book'+i))
        {
            var eleDiv = document.createElement("div");
            eleDiv.setAttribute("id","book"+i);
            if(i%4!=0)
              eleDiv.setAttribute("class","books");
            else
              eleDiv.setAttribute("class","lastBooks");
            booksBox.appendChild(eleDiv);
        }
        i++;
      }
};
function createImages(numberOfImgs, thumbnails){
    var i=0;
    while(i<numberOfImgs)
    {
        if(!document.getElementById('bookImg'+i))
        {
            var eleDiv = document.getElementById("book"+i);
            var eleImg = document.createElement("IMG");
            eleImg.setAttribute("id","bookImg"+i);
            eleImg.setAttribute("class","thumbnails");
            eleImg.src = thumbnails[i];
            eleDiv.appendChild(eleImg);
        }
        i++;
    }
};
function createBookInfo(numberOfBooks, titles, ratings, publicatedDates){
    var i=0;
    while(i<numberOfBooks)
    {
        if(!document.getElementById('bookTitle'+i)&&!document.getElementById('bookInfo'+i))
        {
            var eleDiv = document.getElementById("book"+i);
            var eleTitle = document.createElement("p");
            var eleInfo = document.createElement("p");
            eleTitle.setAttribute("id","bookTitle"+i);
            eleTitle.setAttribute("class","titles");
            eleTitle.innerHTML = "\"" + titles[i] + "\"";
            eleInfo.setAttribute("id","bookInfo"+i);
            eleInfo.setAttribute("class","bookInfo");
            eleInfo.innerHTML = "Rating: " + ratings[i] + "<br>Publication date:<br>" + publicatedDates[i];;
            eleDiv.appendChild(eleTitle);
            eleDiv.appendChild(eleInfo);
        }
        i++;
    }
};
function httpGetAsync(url)
{
  return new Promise(function (resolve,reject){
      var request = new XMLHttpRequest();
      request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                resolve(request.responseText);
                }
            else
                console.log("The computer appears to be offline.");
        }
        };
      request.open("GET", url , true);
      request.send();
    });
};
function getFormatedDate(dateJ){
  var date = new Date(dateJ);
  var year = date.getFullYear();
  var month = (date.getMonth()+1);
  var day = date.getDate();
  if(month<10)
    month = "0"+month;
  if(day<10)
    day = "0"+day;
  return year + "-" + month + "-" + day;
};
