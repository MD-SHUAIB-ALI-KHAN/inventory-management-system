const API = '/api/items';
const f = ['name','category','batchNo','quantity','price','supplier','purchaseDate','expiryDate'];
const $ = id => document.getElementById(id);
const esc = s => (s ?? '').toString().replace(/[&<>"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));

// fetch wrapper: send to login page if session expired
async function call(url, opt) {
  const r = await fetch(url, opt);
  if (r.status === 401) { location.href = '/login.html'; throw new Error('Not logged in'); }
  return r;
}
async function errMsg(res) {
  try { const d = await res.json(); return d.errors ? Object.values(d.errors).join('\n') : d.message; }
  catch { return 'Request failed'; }
}
async function load() {
  const q = $('search').value.trim();
  const res = await call(q ? `${API}?search=${encodeURIComponent(q)}` : API);
  render(await res.json());
}
function render(items) {
  const today = new Date().toISOString().slice(0, 10);
  $('rows').innerHTML = items.map((i, n) => `
    <tr class="${i.expiryDate && i.expiryDate < today ? 'exp' : ''}">
      <td>${n + 1}</td><td>${esc(i.name)}</td><td>${esc(i.category)}</td><td>${esc(i.batchNo)}</td>
      <td class="${i.quantity < 10 ? 'low' : ''}">${i.quantity}</td><td>₹${i.price.toFixed(2)}</td>
      <td>${esc(i.supplier)}</td><td>${i.purchaseDate || ''}</td><td>${i.expiryDate || ''}</td>
      <td><button class="edit" onclick="edit(${i.id})">Edit</button>
          <button class="del" onclick="del(${i.id})">Delete</button></td></tr>`).join('');
  const val = items.reduce((s, i) => s + i.quantity * i.price, 0);
  $('stats').innerHTML = `<span>Items: ${items.length}</span><span>Total qty: ${items.reduce((s, i) => s + i.quantity, 0)}</span><span>Stock value: ₹${val.toFixed(2)}</span><span>Low stock (&lt;10): ${items.filter(i => i.quantity < 10).length}</span>`;
}
$('itemForm').onsubmit = async e => {
  e.preventDefault();
  const body = {};
  f.forEach(k => body[k] = $(k).value || null);
  body.quantity = +body.quantity; body.price = +body.price;
  const id = $('id').value;
  const res = await call(id ? `${API}/${id}` : API, {
    method: id ? 'PUT' : 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(body)
  });
  if (!res.ok) return alert(await errMsg(res));
  reset(); load();
};
async function edit(id) {
  const i = await (await call(`${API}/${id}`)).json();
  $('id').value = i.id; f.forEach(k => $(k).value = i[k] ?? '');
  $('formTitle').textContent = 'Edit Item'; window.scrollTo(0, 0);
}
async function del(id) {
  if (!confirm('Delete this item?')) return;
  const res = await call(`${API}/${id}`, {method: 'DELETE'});
  if (!res.ok) alert(await errMsg(res));
  load();
}
function reset() { $('itemForm').reset(); $('id').value = ''; $('formTitle').textContent = 'Add Item'; }
$('cancel').onclick = reset;
$('search').oninput = load;
load();
