const observeProducts = () => {
  const price = document.getElementById('max-price').value;
  const tBody = document.getElementById('product-table-body');

  var source = new EventSource("/product/stream/" + price);
  source.onmessage = (evt) => {
    let product = JSON.parse(evt.data);
    let row = `
        <th scope="row">${product.id}</th>
        <td>${product.name}</td>
        <td>${product.price}</td>
    `
    let tr = document.createElement('tr');
    tr.innerHTML = row;
    tBody.appendChild(tr);
  }
}

document.getElementById('notify-btn').addEventListener('click', observeProducts);