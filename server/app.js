const express = require('express');
const app = express();
const fs = require("fs");

const shopList = fs.readFileSync("./shop_filter.json");

const shopData = fs.readFileSync("./shopData.json");

function random(min, max) {
  return parseInt(Math.random() * (max - min) + min);
}

app.get('/shopList', (req, res) => {
  const list = JSON.parse(shopList);
  let data;
  if (!req.query["start"]) {
    data = list.slice(0, 10);
  } else {
    let start = parseInt(req.query["start"]);
    data = list.slice(start, start + 10);
  }
  return res.json({
    code: 0,
    msg: "success",
    data: data
  });
});

app.get('/product', (req, res) => {
  const list = JSON.parse(shopData);
  const index = random(0, list.length);
  const data = list.slice(index, index + 1)[0];
  return res.json({
    code: 0,
    msg: "success",
    data
  });
});

app.listen(34565, () => {
  console.log(`server run on 34565 port http://127.0.0.1:34565`);
});