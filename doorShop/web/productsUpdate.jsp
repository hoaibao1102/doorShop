<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.Products, java.util.List, dto.ProductImages" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Product Management</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
    </head>
    <body class="bg-light">
        <%
            Products product = (Products) request.getAttribute("product");
            String messageAddProduct = (String) request.getAttribute("messageAddProduct");
            String checkErrorAddProduct = (String) request.getAttribute("checkErrorAddProduct");
            String checkErrorEditProduct = (String) request.getAttribute("checkErrorEditProduct");
            List<ProductImages> productImages = (List<ProductImages>) request.getAttribute("productImages");
        %>
        <div class="container mt-5">
            <h2 class="mb-4"><%= (product != null) ? "Edit Product" : "Add New Product" %></h2>

            <!-- Thông báo -->
            <% if (messageAddProduct != null) { %>
            <div class="alert alert-success"><%= messageAddProduct %></div>
            <% } else if (checkErrorAddProduct != null) { %>
            <div class="alert alert-danger"><%= checkErrorAddProduct %></div>
            <% } else if (checkErrorEditProduct != null) { %>
            <div class="alert alert-danger"><%= checkErrorEditProduct %></div>
            <% } %>

            <!-- Form -->
            <form action="MainController" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="<%= (product != null) ? "updateProduct" : "addProduct" %>"/>
                <% if (product != null) { %>
                <input type="hidden" name="productId" value="<%= product.getProduct_id() %>"/>
                <% } %>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Category ID</label>
                        <input type="number" name="categoryId" class="form-control"
                               value="<%= (product != null) ? product.getCategory_id() : "" %>" required/>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Brand ID</label>
                        <input type="number" name="brandId" class="form-control"
                               value="<%= (product != null) ? product.getBrand_id() : "" %>" required/>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label">Product Name</label>
                    <input type="text" name="name" class="form-control"
                           value="<%= (product != null) ? product.getName() : "" %>" required/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Price</label>
                    <input type="number" step="0.01" name="price" class="form-control"
                           value="<%= (product != null) ? product.getPrice() : "" %>" required/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Specification (HTML)</label>
                    <textarea id="editor" name="spec_html" class="form-control" rows="10">
                        <%= (product != null) ? product.getSpec_html() : "" %>
                    </textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select">
                        <option value="active"   <%= (product != null && "active".equals(product.getStatus())) ? "selected" : "" %>>Active</option>
                        <option value="inactive" <%= (product != null && "inactive".equals(product.getStatus())) ? "selected" : "" %>>Inactive</option>
                        <option value="prominent"  <%= (product != null && "prominent".equals(product.getStatus())) ? "selected" : "" %>>Prominent</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Upload Images (max 4)</label>
                    <div class="row g-2">
                        <div class="col-md-3">
                            <input type="file" name="imageFile1" class="form-control" accept="image/*"/>
                        </div>
                        <div class="col-md-3">
                            <input type="file" name="imageFile2" class="form-control" accept="image/*"/>
                        </div>
                        <div class="col-md-3">
                            <input type="file" name="imageFile3" class="form-control" accept="image/*"/>
                        </div>
                        <div class="col-md-3">
                            <input type="file" name="imageFile4" class="form-control" accept="image/*"/>
                        </div>
                    </div>

                    <% if (productImages != null && !productImages.isEmpty()) { %>
                    <div class="mt-3">
                        <p>Current Images:</p>
                        <div class="d-flex flex-wrap gap-2">
                            <% for (ProductImages img : productImages) { %>
                            <img src="<%= request.getContextPath() %>/<%= img.getImage_url() %>"
                                 class="img-thumbnail" style="max-width:150px;"/>
                            <% } %>
                        </div>
                    </div>
                    <% } %>
                </div>
                <button type="submit" class="btn btn-primary">
                    <%= (product != null) ? "Update Product" : "Add Product" %>
                </button>
                <a href="welcome.jsp" class="btn btn-secondary">Back to List</a>
            </form>
        </div>
        <!-- TinyMCE -->
        <script src="https://cdn.tiny.cloud/1/9q1kybnxbgq2f5l3c8palpboawfgsnqsdd53b7gk5ny3dh19/tinymce/6/tinymce.min.js" referrerpolicy="origin"></script>
        <script>
            tinymce.init({
                selector: '#editor',
                height: 400,
                plugins: 'image link lists table code',
                toolbar: 'undo redo | bold italic underline | alignleft aligncenter alignright | ' +
                        'bullist numlist | link image | table | code',
                menubar: 'file edit view insert format tools table help',
                automatic_uploads: true,
                images_upload_url: '<%= request.getContextPath() %>/UploadImageServlet',
                images_upload_credentials: true,
                convert_urls: false
            });
        </script>
    </body>
</html>