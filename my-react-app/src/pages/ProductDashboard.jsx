import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  TextField,
  Typography,
  Container,
  Grid,
  Dialog,
  ButtonGroup,
  CircularProgress,
} from '@mui/material';
import { Plus, Edit2, Trash2, Search } from 'lucide-react';

const API_BASE = 'http://localhost:8080';

export default function ProductDashboard({ user }) {
  const [products, setProducts] = useState([]);
  const [searchFilter, setSearchFilter] = useState('personal');
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    price: '',
    expirationDate: '',
  });

  useEffect(() => {
    fetchProducts();
  }, [searchFilter]);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const endpoint =
        searchFilter === 'global'
          ? `/users/${user.id}/products/global`
          : `/users/${user.id}/products`;

      const url = `${API_BASE}${endpoint}`;
      console.log('Fetching from:', url, 'User ID:', user.id);

      const response = await fetch(url, {
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
          'X-User-ID': String(user.id),
          'Authorization': `Bearer ${user.id}`,
        },
      });

      console.log('Response status:', response.status);
      if (!response.ok) {
        const errorText = await response.text();
        console.error('Error response:', errorText);
        throw new Error(`Failed to fetch products: ${response.status}`);
      }
      const data = await response.json();
      setProducts(Array.isArray(data) ? data : [data]);

    } catch (err) {
      console.error('Fetch error:', err.message);
      alert(
        `Error: ${err.message}\n\nMake sure:\n1. Backend is running on http://localhost:8080\n2. You are logged in\n3. Check browser console for details`
      );
      setProducts([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
  if (!searchTerm.trim()) {
    fetchProducts();
    return;
  }

  setLoading(true);
  try {
    const endpoint =
      searchFilter === 'global'
        ? `/users/${user.id}/products/global/name/${encodeURIComponent(searchTerm)}`
        : `/users/${user.id}/products/name/${encodeURIComponent(searchTerm)}`;

    const response = await fetch(`${API_BASE}${endpoint}`, {
      credentials: 'include',
    });

    if (!response.ok) throw new Error('Product not found');

    const data = await response.json();
    setProducts(Array.isArray(data) ? data : [data]);
  } catch (err) {
    console.error(err);
    setProducts([]);
  } finally {
    setLoading(false);
  }
};

  const handleAddProduct = async () => {
    if (!formData.name || !formData.price || !formData.expirationDate) {
      alert('Please fill in all fields');
      return;
    }

    try {
      const response = await fetch(`${API_BASE}/users/${user.id}/products`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
          'X-User-ID': String(user.id),
          'Authorization': `Bearer ${user.id}`,
        },
        body: JSON.stringify({
          name: formData.name,
          price: parseFloat(formData.price),
          expirationDate: formData.expirationDate,
        }),
      });

      if (!response.ok) throw new Error('Failed to add product');
      setFormData({ name: '', price: '', expirationDate: '' });
      setShowAddForm(false);
      fetchProducts();
    } catch (err) {
      console.error(err);
      alert('Error adding product: ' + err.message);
    }
  };

  const handleDeleteProduct = async (id) => {
    if (!window.confirm('Are you sure you want to delete this product?')) return;

    try {
      const response = await fetch(`${API_BASE}/users/${user.id}/products/${id}`, {
        method: 'DELETE',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
          'X-User-ID': String(user.id),
          'Authorization': `Bearer ${user.id}`,
        },
      });

      if (!response.ok) throw new Error('Failed to delete product');
      fetchProducts();
    } catch (err) {
      console.error(err);
      alert('Error deleting product: ' + err.message);
    }
  };

  const handleUpdateProduct = async () => {
  if (!formData.name || !formData.price || !formData.expirationDate) {
    alert('Please fill in all fields');
    return;
  }

  try {
    const params = new URLSearchParams({
      name: formData.name,
      price: parseFloat(formData.price),
      expiration_date: formData.expirationDate, // 👈 MUST MATCH
    });

    const response = await fetch(
      `${API_BASE}/users/${user.id}/products/${editingId}?${params.toString()}`,
      {
        method: 'PUT',
        credentials: 'include',
        headers: {
          'X-User-ID': String(user.id),
          'Authorization': `Bearer ${user.id}`,
        },
      }
    );

    if (!response.ok) {
      const text = await response.text();
      throw new Error(text);
    }

    setEditingId(null);
    setFormData({ name: '', price: '', expirationDate: '' });
    fetchProducts();
  } catch (err) {
    console.error(err);
    alert('Error updating product: ' + err.message);
  }
};



  const filteredProducts = products;

  return (
    <Box sx={{ py: 4, bgcolor: '#f5f5f5', height: '100%', width: '100%', overflow: 'auto' }}>
      <Container maxWidth="lg">
        <Card sx={{ mb: 3, boxShadow: 2 }}>
          <CardContent>
            <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
              <ButtonGroup variant="outlined">
                <Button
                  onClick={() => {
                    setSearchFilter('personal');
                    setSearchTerm('');
                  }}
                  variant={searchFilter === 'personal' ? 'contained' : 'outlined'}
                  sx={{
                    background:
                      searchFilter === 'personal'
                        ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                        : 'white',
                    color: searchFilter === 'personal' ? 'white' : '#667eea',
                  }}
                >
                  My Products
                </Button>
                <Button
                  onClick={() => {
                    setSearchFilter('global');
                    setSearchTerm('');
                  }}
                  variant={searchFilter === 'global' ? 'contained' : 'outlined'}
                  sx={{
                    background:
                      searchFilter === 'global'
                        ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                        : 'white',
                    color: searchFilter === 'global' ? 'white' : '#667eea',
                  }}
                >
                  Global Products
                </Button>
              </ButtonGroup>
              <Button
                onClick={() => setShowAddForm(!showAddForm)}
                variant="contained"
                sx={{
                  ml: 'auto',
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  color: 'white',
                  gap: 1,
                }}
              >
                <Plus size={18} /> Add Product
              </Button>
            </Box>

            {showAddForm && (
              <Box sx={{ border: '1px solid #e0e0e0', p: 2, mb: 2, borderRadius: 1 }}>
                <TextField
                  type="text"
                  label="Product Name"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  fullWidth
                  margin="normal"
                />
                <TextField
                  type="number"
                  label="Price"
                  value={formData.price}
                  onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                  fullWidth
                  margin="normal"
                  inputProps={{ step: '0.01' }}
                />
                <TextField
                  type="date"
                  label="Expiration Date"
                  value={formData.expirationDate}
                  onChange={(e) =>
                    setFormData({ ...formData, expirationDate: e.target.value })
                  }
                  fullWidth
                  margin="normal"
                  InputLabelProps={{ shrink: true }}
                />
                <Box sx={{ display: 'flex', gap: 1, mt: 2 }}>
                  <Button
                    onClick={handleAddProduct}
                    variant="contained"
                    sx={{
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      color: 'white',
                    }}
                  >
                    Save
                  </Button>
                  <Button onClick={() => setShowAddForm(false)} variant="outlined">
                    Cancel
                  </Button>
                </Box>
              </Box>
            )}

            <Box sx={{ display: 'flex', gap: 2 }}>
              <TextField
                type="text"
                placeholder="Search products..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                fullWidth
                variant="outlined"
                size="small"
              />
              <Button
                onClick={handleSearch}
                variant="contained"
                sx={{
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  color: 'white',
                  gap: 1,
                }}
              >
                <Search size={18} />
              </Button>
            </Box>
          </CardContent>
        </Card>

        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
            <CircularProgress />
          </Box>
        )}

        <Grid container spacing={2}>
          {filteredProducts.map((product) => (
            <Grid item xs={12} sm={6} md={4} key={product.id}>
              <Card sx={{ height: '100%', boxShadow: 2, '&:hover': { boxShadow: 4 } }}>
                <CardContent>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 1 }}>
                    {product.name}
                  </Typography>
                  <Typography variant="body2" sx={{ color: 'gray', mb: 0.5 }}>
                    Price: ${parseFloat(product.price).toFixed(2)}
                  </Typography>
                  <Typography variant="body2" sx={{ color: 'gray', mb: 2 }}>
                    Expires: {product.expirationDate}
                  </Typography>

                  <Box sx={{ display: 'flex', gap: 1 }}>
                    <Button
                      onClick={() => {
                        setEditingId(product.id);
                        setFormData({
                          name: product.name,
                          price: product.price,
                          expirationDate: product.expirationDate,
                        });
                      }}
                      variant="contained"
                      size="small"
                      sx={{
                        background: '#ff9800',
                        color: 'white',
                        flex: 1,
                        gap: 0.5,
                      }}
                    >
                      <Edit2 size={14} /> Edit
                    </Button>
                    <Button
                      onClick={() => handleDeleteProduct(product.id)}
                      variant="contained"
                      size="small"
                      sx={{
                        background: '#f44336',
                        color: 'white',
                        flex: 1,
                        gap: 0.5,
                      }}
                    >
                      <Trash2 size={14} /> Delete
                    </Button>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>

        {!loading && filteredProducts.length === 0 && (
          <Box sx={{ textAlign: 'center', py: 4 }}>
            <Typography variant="body1" sx={{ color: 'gray' }}>
              No products found
            </Typography>
          </Box>
        )}

        <Dialog open={editingId !== null} onClose={() => setEditingId(null)} maxWidth="sm" fullWidth>
          <Box sx={{ p: 3 }}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2 }}>
              Edit Product
            </Typography>
            <TextField
              type="text"
              label="Product Name"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              fullWidth
              margin="normal"
            />
            <TextField
              type="number"
              label="Price"
              value={formData.price}
              onChange={(e) => setFormData({ ...formData, price: e.target.value })}
              fullWidth
              margin="normal"
              inputProps={{ step: '0.01' }}
            />
            <TextField
              type="date"
              label="Expiration Date"
              value={formData.expirationDate}
              onChange={(e) =>
                setFormData({ ...formData, expirationDate: e.target.value })
              }
              fullWidth
              margin="normal"
              InputLabelProps={{ shrink: true }}
            />
            <Box sx={{ display: 'flex', gap: 1, mt: 3 }}>
              <Button
                onClick={handleUpdateProduct}
                variant="contained"
                fullWidth
                sx={{
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  color: 'white',
                }}
              >
                Update
              </Button>
              <Button onClick={() => setEditingId(null)} variant="outlined" fullWidth>
                Cancel
              </Button>
            </Box>
          </Box>
        </Dialog>
      </Container>
    </Box>
  );
}