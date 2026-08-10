import pandas as pd
import joblib
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline

# 1. Cargar el dataset real entregado por el equipo de datos
df = pd.read_csv("base_contenidos.csv")

# Combinar título y texto para mejorar la precisión del modelo
df['contenido_completo'] = df['titulo'].fillna('') + " " + df['texto'].fillna('')

X = df['contenido_completo']
y = df['categoria']

# 2. Pipeline de Machine Learning (TF-IDF + Regresión Logística)
pipeline = Pipeline([
    ('tfidf', TfidfVectorizer(stop_words=None)),
    ('clf', LogisticRegression())
])

# 3. Entrenamiento con los 210 registros
pipeline.fit(X, y)

# 4. Guardar el modelo entrenado
joblib.dump(pipeline, 'modelo_techmind.joblib')

print("✅ ¡Éxito! El modelo ha sido entrenado con los 210 registros de 'base_contenidos.csv'.")
print("✅ Archivo 'modelo_techmind.joblib' generado y listo para la API.")