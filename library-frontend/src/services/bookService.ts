import { api } from 'boot/axios';
import type { BookType } from 'src/types/bookType';

export async function listAllBook() {
  const response = await api.get('/books');

  return response.data.obj;
}

export async function createBook(book: BookType) {
  const response = await api.post('/books', book);

  return response.data.obj;
}

export async function updateBook(id: string, book: BookType) {
  const response = await api.put(`/books/${id}`, book);

  return response.data.obj;
}

export async function removeBook(id: string) {
  await api.delete(`/books/${id}`);
}
