#!/bin/bash

# Start Ollama in the background
/bin/ollama serve &
pid=$!

# Wait for Ollama to start
sleep 5

# Run the model with the specified keep-alive time
echo "Starting ${OLLAMA_MODEL} model with keep-alive time of ${OLLAMA_KEEP_ALIVE_TIME}..."
ollama run "${OLLAMA_MODEL}" --keepalive "${OLLAMA_KEEP_ALIVE_TIME}"

# Wait for Ollama process to finish
wait $pid